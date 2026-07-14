package com.smarthome.services;
import com.smarthome.generated.*;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.*;
import java.util.logging.Logger;

public class SolarEnergyServiceImpl extends SolarEnergyServiceGrpc.SolarEnergyServiceImplBase {
    private static final Logger logger = Logger.getLogger(SolarEnergyServiceImpl.class.getName());
    private final Map<String, DailySummaryResponse> summaryStore = new HashMap<>();

    @Override
    public StreamObserver<SolarReading> uploadSolarReadings(StreamObserver<SolarSummary> responseObserver) {
        return new StreamObserver<SolarReading>() {
            private final List<SolarReading> readings = new ArrayList<>();
            private String panelId = "";
            private double totalGenerated = 0, peakGeneration = 0, totalIrradiance = 0;
            private String firstTimestamp = "", lastTimestamp = "";

            @Override
            public void onNext(SolarReading reading) {
                // ── Cancellation check on each incoming reading ─────────
                if (Context.current().isCancelled()) {
                    logger.warning("UploadSolarReadings cancelled by client mid-stream");
                    responseObserver.onError(Status.CANCELLED
                            .withDescription("Upload cancelled by client after " + readings.size() + " readings")
                            .asRuntimeException());
                    return;
                }
                logger.info("Received solar reading: panel=" + reading.getPanelId() + " power=" + reading.getGeneratedPower() + " kWh");
                if (reading.getPanelId().isEmpty() || reading.getGeneratedPower() < 0) {
                    logger.warning("Skipping invalid reading");
                    return;
                }
                if (panelId.isEmpty()) { panelId = reading.getPanelId(); firstTimestamp = reading.getTimestamp(); }
                readings.add(reading);
                totalGenerated += reading.getGeneratedPower();
                totalIrradiance += reading.getIrradiance();
                lastTimestamp = reading.getTimestamp();
                if (reading.getGeneratedPower() > peakGeneration) peakGeneration = reading.getGeneratedPower();
            }

            @Override
            public void onError(Throwable t) {
                logger.severe("UploadSolarReadings stream error: " + t.getMessage());
                // Client cancelled or connection dropped — no response needed
            }

            @Override
            public void onCompleted() {
                if (readings.isEmpty()) {
                    responseObserver.onError(Status.INVALID_ARGUMENT
                            .withDescription("No valid readings received").asRuntimeException());
                    return;
                }
                double avgIrradiance = Math.round((totalIrradiance / readings.size()) * 100.0) / 100.0;
                totalGenerated = Math.round(totalGenerated * 100.0) / 100.0;
                peakGeneration = Math.round(peakGeneration * 100.0) / 100.0;

                SolarSummary summary = SolarSummary.newBuilder()
                        .setTotalReadings(readings.size()).setTotalGeneratedKwh(totalGenerated)
                        .setPeakGenerationKwh(peakGeneration).setAverageIrradiance(avgIrradiance)
                        .setMessage("Daily upload complete for panel: " + panelId).build();

                String date = firstTimestamp.length() >= 10 ? firstTimestamp.substring(0, 10) : "unknown";
                summaryStore.put(panelId + "_" + date, DailySummaryResponse.newBuilder()
                        .setTotalGeneratedKwh(totalGenerated).setPeakGenerationKwh(peakGeneration)
                        .setStartTime(firstTimestamp).setEndTime(lastTimestamp)
                        .setMessage("Stored summary for " + panelId + " on " + date).build());

                logger.info("Upload complete: " + readings.size() + " readings, " + totalGenerated + " kWh total");
                responseObserver.onNext(summary);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getDailySummary(DailySummaryRequest request, StreamObserver<DailySummaryResponse> responseObserver) {
        logger.info("GetDailySummary: panel=" + request.getPanelId() + " date=" + request.getDate());
        try {
            if (Context.current().isCancelled()) {
                responseObserver.onError(Status.CANCELLED.withDescription("Request cancelled by client").asRuntimeException()); return;
            }
            if (request.getPanelId().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("panel_id cannot be empty").asRuntimeException()); return;
            }
            if (request.getDate().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("date cannot be empty — use YYYY-MM-DD").asRuntimeException()); return;
            }
            DailySummaryResponse stored = summaryStore.get(request.getPanelId() + "_" + request.getDate());
            if (stored == null) {
                responseObserver.onError(Status.NOT_FOUND.withDescription("No summary for panel: "
                        + request.getPanelId() + " on " + request.getDate()).asRuntimeException()); return;
            }
            responseObserver.onNext(stored);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Error: " + e.getMessage()).asRuntimeException());
        }
    }
}