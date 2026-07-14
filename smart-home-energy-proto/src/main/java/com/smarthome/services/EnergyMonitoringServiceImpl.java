package com.smarthome.services;

import com.smarthome.generated.*;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

/**
 * Energy Monitoring Service Implementation
 *
 * Additional complexity:
 *   - Maintains a full usage history per household (not just the latest reading)
 *   - Calculates daily average and cumulative consumption
 *   - Applies dynamic thresholds: NORMAL < 0.8 kWh, HIGH < 1.4 kWh, CRITICAL >= 1.4 kWh
 *   - Cancellation checks on every RPC including mid-stream
 */
public class EnergyMonitoringServiceImpl extends EnergyMonitoringServiceGrpc.EnergyMonitoringServiceImplBase {

    private static final Logger logger = Logger.getLogger(EnergyMonitoringServiceImpl.class.getName());

    // Stores the latest reading per household
    private final Map<String, EnergyReading> latestReadings = new HashMap<>();

    // Stores full usage history per household for cumulative analysis
    private final Map<String, List<Double>> usageHistory = new HashMap<>();

    @Override
    public void recordEnergyUsage(RecordUsageRequest request, StreamObserver<RecordUsageResponse> responseObserver) {
        logger.info("RecordEnergyUsage: " + request.getHouseholdId());
        try {
            if (Context.current().isCancelled()) {
                responseObserver.onError(Status.CANCELLED.withDescription("Request cancelled by client").asRuntimeException()); return;
            }
            if (request.getHouseholdId().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("household_id cannot be empty").asRuntimeException()); return;
            }
            if (request.getEnergyUsed() < 0) {
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("energy_used cannot be negative").asRuntimeException()); return;
            }

            String status = classifyUsage(request.getEnergyUsed());

            // Save latest reading
            EnergyReading reading = EnergyReading.newBuilder()
                    .setEnergyUsed(request.getEnergyUsed()).setTimestamp(request.getTimestamp())
                    .setStatus(status).build();
            latestReadings.put(request.getHouseholdId(), reading);

            // Accumulate history for average/cumulative tracking
            usageHistory.computeIfAbsent(request.getHouseholdId(), k -> new ArrayList<>())
                    .add(request.getEnergyUsed());

            List<Double> history = usageHistory.get(request.getHouseholdId());
            double cumulative = history.stream().mapToDouble(Double::doubleValue).sum();
            double average    = cumulative / history.size();

            String message = String.format(
                    "Recorded for %s: %.2f kWh [%s] | Cumulative: %.2f kWh | Avg: %.2f kWh over %d readings",
                    request.getHouseholdId(), request.getEnergyUsed(), status,
                    cumulative, average, history.size());

            responseObserver.onNext(RecordUsageResponse.newBuilder().setSuccess(true).setMessage(message).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Error: " + e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getLatestReading(LatestReadingRequest request, StreamObserver<LatestReadingResponse> responseObserver) {
        logger.info("GetLatestReading: " + request.getHouseholdId());
        try {
            if (Context.current().isCancelled()) {
                responseObserver.onError(Status.CANCELLED.withDescription("Request cancelled by client").asRuntimeException()); return;
            }
            if (request.getHouseholdId().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("household_id cannot be empty").asRuntimeException()); return;
            }
            EnergyReading saved = latestReadings.get(request.getHouseholdId());
            if (saved == null) {
                responseObserver.onError(Status.NOT_FOUND.withDescription("No readings found for: " + request.getHouseholdId()).asRuntimeException()); return;
            }
            responseObserver.onNext(LatestReadingResponse.newBuilder()
                    .setEnergyUsed(saved.getEnergyUsed()).setTimestamp(saved.getTimestamp()).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Error: " + e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void streamLiveReadings(StreamRequest request, StreamObserver<EnergyReading> responseObserver) {
        logger.info("StreamLiveReadings: " + request.getHouseholdId());
        try {
            if (request.getHouseholdId().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("household_id cannot be empty").asRuntimeException()); return;
            }
            int intervalMs = Math.max(request.getIntervalSeconds(), 1) * 1000;

            for (int i = 1; i <= 10; i++) {
                // ── Cancellation check every iteration ─────────────────
                if (Context.current().isCancelled()) {
                    logger.warning("StreamLiveReadings cancelled after " + (i - 1) + " readings");
                    responseObserver.onError(Status.CANCELLED
                            .withDescription("Stream cancelled by client after " + (i - 1) + " readings")
                            .asRuntimeException());
                    return;
                }
                // Simulate time-of-day variation in consumption
                double baseUsage = (i <= 3 || i >= 9) ? 0.3 : 0.8; // lower in morning/evening
                double usage = Math.round((baseUsage + Math.random() * 0.9) * 100.0) / 100.0;
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                String status = classifyUsage(usage);

                EnergyReading reading = EnergyReading.newBuilder()
                        .setEnergyUsed(usage).setTimestamp(timestamp).setStatus(status).build();
                latestReadings.put(request.getHouseholdId(), reading);
                usageHistory.computeIfAbsent(request.getHouseholdId(), k -> new ArrayList<>()).add(usage);

                responseObserver.onNext(reading);
                logger.info("Streamed reading " + i + ": " + usage + " kWh [" + status + "]");
                Thread.sleep(intervalMs);
            }
            responseObserver.onCompleted();
            logger.info("StreamLiveReadings completed for: " + request.getHouseholdId());

        } catch (InterruptedException e) {
            logger.warning("StreamLiveReadings interrupted");
            responseObserver.onError(Status.CANCELLED.withDescription("Stream interrupted").asRuntimeException());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Error: " + e.getMessage()).asRuntimeException());
        }
    }

    private String classifyUsage(double kWh) {
        if (kWh < 0.8) return "NORMAL";
        if (kWh < 1.4) return "HIGH";
        return "CRITICAL";
    }
}