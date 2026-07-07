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
 * Smart Appliance Control Service Implementation
 *
 * Provides two RPC methods:
 *   1. ControlAppliance   - Bidirectional Streaming RPC
 *   2. GetApplianceStatus - Unary RPC
 */
public class ApplianceControlServiceImpl extends ApplianceControlServiceGrpc.ApplianceControlServiceImplBase {

    private static final Logger logger = Logger.getLogger(ApplianceControlServiceImpl.class.getName());

    // In-memory store of current appliance states: applianceId -> ApplianceStatusResponse
    private final Map<String, ApplianceStatusResponse> applianceStore = new HashMap<>();

    // ?????????????????????????????????????????????????????????????
    //  1. Bidirectional Streaming RPC: ControlAppliance
    //     Client sends commands, server streams back status updates
    //     simultaneously. Both sides can send at any time.
    // ?????????????????????????????????????????????????????????????
    @Override
    public StreamObserver<ApplianceCommand> controlAppliance(StreamObserver<ApplianceStatus> responseObserver) {

        return new StreamObserver<ApplianceCommand>() {

            // Called each time the client sends a command
            @Override
            public void onNext(ApplianceCommand command) {
                logger.info("Received command: " + command.getAction()
                        + " for appliance: " + command.getApplianceName()
                        + " [" + command.getApplianceId() + "]"
                        + " in room: " + command.getRoomLocation());

                // Validate the command
                if (command.getApplianceId().isEmpty()) {
                    logger.warning("Command received with empty appliance_id — sending error status");
                    ApplianceStatus errorStatus = ApplianceStatus.newBuilder()
                            .setApplianceName(command.getApplianceName())
                            .setStatus("ERROR")
                            .setPowerConsumption(0.0)
                            .setMessage("appliance_id cannot be empty")
                            .setTimestamp(now())
                            .build();
                    responseObserver.onNext(errorStatus);
                    return;
                }

                if (!isValidAction(command.getAction())) {
                    logger.warning("Invalid action received: " + command.getAction());
                    ApplianceStatus errorStatus = ApplianceStatus.newBuilder()
                            .setApplianceId(command.getApplianceId())
                            .setApplianceName(command.getApplianceName())
                            .setStatus("ERROR")
                            .setPowerConsumption(0.0)
                            .setMessage("Invalid action '" + command.getAction()
                                    + "'. Valid actions are: ON, OFF, ECO")
                            .setTimestamp(now())
                            .build();
                    responseObserver.onNext(errorStatus);
                    return;
                }

                // Process the command — calculate power consumption based on action
                double powerConsumption = calculatePowerConsumption(
                        command.getApplianceName(), command.getAction());

                String timestamp = now();

                // Build the status update to stream back
                ApplianceStatus status = ApplianceStatus.newBuilder()
                        .setApplianceId(command.getApplianceId())
                        .setApplianceName(command.getApplianceName())
                        .setStatus(command.getAction())
                        .setPowerConsumption(powerConsumption)
                        .setMessage(command.getApplianceName() + " in " + command.getRoomLocation()
                                + " set to " + command.getAction()
                                + " | Drawing " + powerConsumption + "W")
                        .setTimestamp(timestamp)
                        .build();

                // Persist the latest state so GetApplianceStatus can return it
                ApplianceStatusResponse stored = ApplianceStatusResponse.newBuilder()
                        .setApplianceId(command.getApplianceId())
                        .setApplianceName(command.getApplianceName())
                        .setStatus(command.getAction())
                        .setPowerConsumption(powerConsumption)
                        .setLastUpdated(timestamp)
                        .setMessage("Last command: " + command.getAction())
                        .build();

                applianceStore.put(command.getApplianceId(), stored);

                // Stream the status back to the client immediately
                responseObserver.onNext(status);
                logger.info("Status sent: " + command.getApplianceName()
                        + " -> " + command.getAction() + " | " + powerConsumption + "W");
            }

            // Called if the client sends an error
            @Override
            public void onError(Throwable t) {
                logger.severe("Error in ControlAppliance stream: " + t.getMessage());
                responseObserver.onError(
                    Status.INTERNAL
                        .withDescription("Client stream error: " + t.getMessage())
                        .asRuntimeException()
                );
            }

            // Called when the client closes the command stream
            @Override
            public void onCompleted() {
                logger.info("Client finished sending commands — closing response stream");
                responseObserver.onCompleted();
            }
        };
    }

    // ?????????????????????????????????????????????????????????????
    //  2. Unary RPC: GetApplianceStatus
    //     Returns the current stored state of a specific appliance
    // ?????????????????????????????????????????????????????????????
    @Override
    public void getApplianceStatus(ApplianceStatusRequest request,
                                   StreamObserver<ApplianceStatusResponse> responseObserver) {
        logger.info("GetApplianceStatus called for appliance: " + request.getApplianceId());

        try {
            if (request.getApplianceId().isEmpty()) {
                responseObserver.onError(
                    Status.INVALID_ARGUMENT
                        .withDescription("appliance_id cannot be empty")
                        .asRuntimeException()
                );
                return;
            }

            ApplianceStatusResponse stored = applianceStore.get(request.getApplianceId());

            if (stored == null) {
                responseObserver.onError(
                    Status.NOT_FOUND
                        .withDescription("No status found for appliance: "
                                + request.getApplianceId()
                                + ". Send a command first via ControlAppliance.")
                        .asRuntimeException()
                );
                return;
            }

            responseObserver.onNext(stored);
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.severe("Error in getApplianceStatus: " + e.getMessage());
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException()
            );
        }
    }

    // ?????????????????????????????????????????????????????????????
    //  Helper: validate action string
    // ?????????????????????????????????????????????????????????????
    private boolean isValidAction(String action) {
        return action.equals("ON") || action.equals("OFF") || action.equals("ECO");
    }

    // ?????????????????????????????????????????????????????????????
    //  Helper: simulate realistic power draw based on appliance type
    //  and current action
    // ?????????????????????????????????????????????????????????????
    private double calculatePowerConsumption(String applianceName, String action) {
        if (action.equals("OFF")) return 0.0;

        String name = applianceName.toLowerCase();
        double basePower;

        if (name.contains("air conditioner") || name.contains("ac")) {
            basePower = action.equals("ECO") ? 800.0 : 1500.0;
        } else if (name.contains("washing machine") || name.contains("washer")) {
            basePower = action.equals("ECO") ? 400.0 : 900.0;
        } else if (name.contains("light") || name.contains("lamp")) {
            basePower = action.equals("ECO") ? 5.0 : 10.0;
        } else if (name.contains("fan")) {
            basePower = action.equals("ECO") ? 20.0 : 50.0;
        } else if (name.contains("fridge") || name.contains("refrigerator")) {
            basePower = action.equals("ECO") ? 80.0 : 150.0;
        } else if (name.contains("dishwasher")) {
            basePower = action.equals("ECO") ? 700.0 : 1200.0;
        } else {
            // Default for unknown appliances
            basePower = action.equals("ECO") ? 100.0 : 200.0;
        }

        return basePower;
    }

    // ?????????????????????????????????????????????????????????????
    //  Helper: get current timestamp in ISO 8601 format
    // ?????????????????????????????????????????????????????????????
    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
