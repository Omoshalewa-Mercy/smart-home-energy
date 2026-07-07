package com.smarthome.services;

import com.smarthome.generated.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Energy Monitoring Service Implementation
 *
 * Provides three RPC methods:
 *   1. RecordEnergyUsage  - Unary RPC
 *   2. GetLatestReading   - Unary RPC
 *   3. StreamLiveReadings - Server Streaming RPC
 */
public class EnergyMonitoringServiceImpl extends EnergyMonitoringServiceGrpc.EnergyMonitoringServiceImplBase {

    private static final Logger logger = Logger.getLogger(EnergyMonitoringServiceImpl.class.getName());

    // In-memory store: householdId -> latest EnergyReading
    private final Map<String, EnergyReading> latestReadings = new HashMap<>();

    // ─────────────────────────────────────────────────────────────
    //  1. Unary RPC: RecordEnergyUsage
    //     Client sends one reading, server saves it and replies
    // ─────────────────────────────────────────────────────────────
    @Override
    public void recordEnergyUsage(RecordUsageRequest request,
                                  StreamObserver<RecordUsageResponse> responseObserver) {
        logger.info("RecordEnergyUsage called for household: " + request.getHouseholdId());

        try {
            // Validate inputs
            if (request.getHouseholdId().isEmpty()) {
                responseObserver.onError(
                    Status.INVALID_ARGUMENT
                        .withDescription("household_id cannot be empty")
                        .asRuntimeException()
                );
                return;
            }
            if (request.getEnergyUsed() < 0) {
                responseObserver.onError(
                    Status.INVALID_ARGUMENT
                        .withDescription("energy_used cannot be negative")
                        .asRuntimeException()
                );
                return;
            }

            // Save the reading
            EnergyReading reading = EnergyReading.newBuilder()
                    .setEnergyUsed(request.getEnergyUsed())
                    .setTimestamp(request.getTimestamp())
                    .setStatus(classifyUsage(request.getEnergyUsed()))
                    .build();

            latestReadings.put(request.getHouseholdId(), reading);

            // Send response
            RecordUsageResponse response = RecordUsageResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Reading recorded for household: " + request.getHouseholdId()
                            + " | Usage: " + request.getEnergyUsed() + " kWh")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.severe("Error in recordEnergyUsage: " + e.getMessage());
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException()
            );
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  2. Unary RPC: GetLatestReading
    //     Client requests the most recent reading for a household
    // ─────────────────────────────────────────────────────────────
    @Override
    public void getLatestReading(LatestReadingRequest request,
                                 StreamObserver<LatestReadingResponse> responseObserver) {
        logger.info("GetLatestReading called for household: " + request.getHouseholdId());

        try {
            if (request.getHouseholdId().isEmpty()) {
                responseObserver.onError(
                    Status.INVALID_ARGUMENT
                        .withDescription("household_id cannot be empty")
                        .asRuntimeException()
                );
                return;
            }

            EnergyReading saved = latestReadings.get(request.getHouseholdId());

            if (saved == null) {
                responseObserver.onError(
                    Status.NOT_FOUND
                        .withDescription("No readings found for household: " + request.getHouseholdId())
                        .asRuntimeException()
                );
                return;
            }

            LatestReadingResponse response = LatestReadingResponse.newBuilder()
                    .setEnergyUsed(saved.getEnergyUsed())
                    .setTimestamp(saved.getTimestamp())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.severe("Error in getLatestReading: " + e.getMessage());
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException()
            );
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  3. Server Streaming RPC: StreamLiveReadings
    //     Server continuously pushes simulated readings to the client
    // ─────────────────────────────────────────────────────────────
    @Override
    public void streamLiveReadings(StreamRequest request,
                                   StreamObserver<EnergyReading> responseObserver) {
        logger.info("StreamLiveReadings started for household: " + request.getHouseholdId()
                + " | Interval: " + request.getIntervalSeconds() + "s");

        try {
            if (request.getHouseholdId().isEmpty()) {
                responseObserver.onError(
                    Status.INVALID_ARGUMENT
                        .withDescription("household_id cannot be empty")
                        .asRuntimeException()
                );
                return;
            }

            int intervalMs = Math.max(request.getIntervalSeconds(), 1) * 1000;

            // Stream 10 simulated live readings
            for (int i = 1; i <= 10; i++) {

                // Simulate fluctuating consumption between 0.3 and 1.8 kWh
                double simulatedUsage = 0.3 + (Math.random() * 1.5);
                simulatedUsage = Math.round(simulatedUsage * 100.0) / 100.0;

                String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                EnergyReading reading = EnergyReading.newBuilder()
                        .setEnergyUsed(simulatedUsage)
                        .setTimestamp(timestamp)
                        .setStatus(classifyUsage(simulatedUsage))
                        .build();

                // Update the latest reading store as readings stream in
                latestReadings.put(request.getHouseholdId(), reading);

                responseObserver.onNext(reading);
                logger.info("Streamed reading " + i + ": " + simulatedUsage + " kWh ["
                        + classifyUsage(simulatedUsage) + "]");

                Thread.sleep(intervalMs);
            }

            responseObserver.onCompleted();
            logger.info("StreamLiveReadings completed for household: " + request.getHouseholdId());

        } catch (InterruptedException e) {
            logger.warning("StreamLiveReadings interrupted for household: " + request.getHouseholdId());
            responseObserver.onError(
                Status.CANCELLED
                    .withDescription("Stream was interrupted")
                    .asRuntimeException()
            );
            Thread.currentThread().interrupt();

        } catch (Exception e) {
            logger.severe("Error in streamLiveReadings: " + e.getMessage());
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException()
            );
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  Helper: classify usage level
    // ─────────────────────────────────────────────────────────────
    private String classifyUsage(double kWh) {
        if (kWh < 0.8)  return "NORMAL";
        if (kWh < 1.4)  return "HIGH";
        return "CRITICAL";
    }
}
