package com.smarthome.services;

import com.smarthome.generated.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Solar Energy Service Implementation
 *
 * Provides two RPC methods:
 *   1. UploadSolarReadings - Client Streaming RPC
 *   2. GetDailySummary     - Unary RPC
 */
public class SolarEnergyServiceImpl extends SolarEnergyServiceGrpc.SolarEnergyServiceImplBase {

    private static final Logger logger = Logger.getLogger(SolarEnergyServiceImpl.class.getName());

    // In-memory store: "panelId_date" -> DailySummaryResponse
    private final Map<String, DailySummaryResponse> summaryStore = new HashMap<>();

    // ?????????????????????????????????????????????????????????????
    //  1. Client Streaming RPC: UploadSolarReadings
    //     Client streams multiple hourly readings throughout the day.
    //     Once the client finishes streaming, the server calculates
    //     and returns a daily generation summary.
    // ?????????????????????????????????????????????????????????????
    @Override
    public StreamObserver<SolarReading> uploadSolarReadings(StreamObserver<SolarSummary> responseObserver) {

        // We return a StreamObserver that handles each reading as it arrives
        return new StreamObserver<SolarReading>() {

            // Accumulate readings as they stream in
            private final List<SolarReading> readings = new ArrayList<>();
            private String panelId = "";
            private double totalGenerated = 0.0;
            private double peakGeneration = 0.0;
            private double totalIrradiance = 0.0;
            private String firstTimestamp = "";
            private String lastTimestamp  = "";
            private boolean hasError = false;

            // Called once for each reading the client sends
            @Override
            public void onNext(SolarReading reading) {
                logger.info("Received solar reading from panel: " + reading.getPanelId()
                        + " | Power: " + reading.getGeneratedPower() + " kWh"
                        + " | Irradiance: " + reading.getIrradiance() + " W/m²");

                // Validate the reading
                if (reading.getPanelId().isEmpty()) {
                    logger.warning("Received reading with empty panel_id — skipping");
                    hasError = true;
                    return;
                }
                if (reading.getGeneratedPower() < 0) {
                    logger.warning("Received negative generated_power — skipping");
                    return;
                }

                // Store panel ID from the first valid reading
                if (panelId.isEmpty()) {
                    panelId = reading.getPanelId();
                    firstTimestamp = reading.getTimestamp();
                }

                // Accumulate totals
                readings.add(reading);
                totalGenerated  += reading.getGeneratedPower();
                totalIrradiance += reading.getIrradiance();
                lastTimestamp    = reading.getTimestamp();

                // Track peak generation
                if (reading.getGeneratedPower() > peakGeneration) {
                    peakGeneration = reading.getGeneratedPower();
                }
            }

            // Called if the client sends an error mid-stream
            @Override
            public void onError(Throwable t) {
                logger.severe("Error received during UploadSolarReadings stream: " + t.getMessage());
                responseObserver.onError(
                    Status.INTERNAL
                        .withDescription("Client stream error: " + t.getMessage())
                        .asRuntimeException()
                );
            }

            // Called when the client has finished sending all readings
            @Override
            public void onCompleted() {
                logger.info("Client finished streaming. Total readings received: " + readings.size());

                if (readings.isEmpty()) {
                    responseObserver.onError(
                        Status.INVALID_ARGUMENT
                            .withDescription("No valid readings were received in the stream")
                            .asRuntimeException()
                    );
                    return;
                }

                // Calculate averages
                double avgIrradiance = totalIrradiance / readings.size();

                // Round values to 2 decimal places
                totalGenerated = Math.round(totalGenerated * 100.0) / 100.0;
                peakGeneration = Math.round(peakGeneration * 100.0) / 100.0;
                avgIrradiance  = Math.round(avgIrradiance  * 100.0) / 100.0;

                // Build the summary
                SolarSummary summary = SolarSummary.newBuilder()
                        .setTotalReadings(readings.size())
                        .setTotalGeneratedKwh(totalGenerated)
                        .setPeakGenerationKwh(peakGeneration)
                        .setAverageIrradiance(avgIrradiance)
                        .setMessage(hasError
                                ? "Upload complete with some skipped readings"
                                : "Daily upload complete for panel: " + panelId)
                        .build();

                // Also persist the summary so GetDailySummary can retrieve it
                String date = firstTimestamp.length() >= 10
                        ? firstTimestamp.substring(0, 10)
                        : "unknown";

                DailySummaryResponse stored = DailySummaryResponse.newBuilder()
                        .setTotalGeneratedKwh(totalGenerated)
                        .setPeakGenerationKwh(peakGeneration)
                        .setStartTime(firstTimestamp)
                        .setEndTime(lastTimestamp)
                        .setMessage("Stored summary for " + panelId + " on " + date)
                        .build();

                summaryStore.put(panelId + "_" + date, stored);
                logger.info("Summary stored for key: " + panelId + "_" + date);

                // Send the summary back to the client and close the stream
                responseObserver.onNext(summary);
                responseObserver.onCompleted();
            }
        };
    }

    // ?????????????????????????????????????????????????????????????
    //  2. Unary RPC: GetDailySummary
    //     Retrieve the stored generation summary for a panel and date
    // ?????????????????????????????????????????????????????????????
    @Override
    public void getDailySummary(DailySummaryRequest request,
                                StreamObserver<DailySummaryResponse> responseObserver) {
        logger.info("GetDailySummary called for panel: " + request.getPanelId()
                + " | Date: " + request.getDate());

        try {
            if (request.getPanelId().isEmpty()) {
                responseObserver.onError(
                    Status.INVALID_ARGUMENT
                        .withDescription("panel_id cannot be empty")
                        .asRuntimeException()
                );
                return;
            }
            if (request.getDate().isEmpty()) {
                responseObserver.onError(
                    Status.INVALID_ARGUMENT
                        .withDescription("date cannot be empty — use YYYY-MM-DD format")
                        .asRuntimeException()
                );
                return;
            }

            String key = request.getPanelId() + "_" + request.getDate();
            DailySummaryResponse stored = summaryStore.get(key);

            if (stored == null) {
                responseObserver.onError(
                    Status.NOT_FOUND
                        .withDescription("No summary found for panel: "
                                + request.getPanelId() + " on date: " + request.getDate())
                        .asRuntimeException()
                );
                return;
            }

            responseObserver.onNext(stored);
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.severe("Error in getDailySummary: " + e.getMessage());
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException()
            );
        }
    }
}
