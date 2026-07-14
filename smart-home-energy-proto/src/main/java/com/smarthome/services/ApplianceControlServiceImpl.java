package com.smarthome.services;
import com.smarthome.generated.*;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ApplianceControlServiceImpl extends ApplianceControlServiceGrpc.ApplianceControlServiceImplBase {
    private static final Logger logger = Logger.getLogger(ApplianceControlServiceImpl.class.getName());
    private final Map<String, ApplianceStatusResponse> applianceStore = new HashMap<>();

    @Override
    public StreamObserver<ApplianceCommand> controlAppliance(StreamObserver<ApplianceStatus> responseObserver) {
        return new StreamObserver<ApplianceCommand>() {

            @Override
            public void onNext(ApplianceCommand command) {
                // ── Cancellation check on every command received ────────
                if (Context.current().isCancelled()) {
                    logger.warning("ControlAppliance stream cancelled by client");
                    responseObserver.onError(Status.CANCELLED
                            .withDescription("Control stream cancelled by client").asRuntimeException());
                    return;
                }
                logger.info("Command: " + command.getAction() + " -> " + command.getApplianceName());

                if (command.getApplianceId().isEmpty()) {
                    responseObserver.onNext(ApplianceStatus.newBuilder()
                            .setApplianceName(command.getApplianceName()).setStatus("ERROR")
                            .setPowerConsumption(0).setMessage("appliance_id cannot be empty")
                            .setTimestamp(now()).build());
                    return;
                }
                if (!isValidAction(command.getAction())) {
                    responseObserver.onNext(ApplianceStatus.newBuilder()
                            .setApplianceId(command.getApplianceId()).setApplianceName(command.getApplianceName())
                            .setStatus("ERROR").setPowerConsumption(0)
                            .setMessage("Invalid action '" + command.getAction() + "'. Valid: ON, OFF, ECO")
                            .setTimestamp(now()).build());
                    return;
                }

                double power = calculatePower(command.getApplianceName(), command.getAction());
                String timestamp = now();

                ApplianceStatus status = ApplianceStatus.newBuilder()
                        .setApplianceId(command.getApplianceId()).setApplianceName(command.getApplianceName())
                        .setStatus(command.getAction()).setPowerConsumption(power)
                        .setMessage(command.getApplianceName() + " in " + command.getRoomLocation()
                                + " set to " + command.getAction() + " | Drawing " + power + "W")
                        .setTimestamp(timestamp).build();

                applianceStore.put(command.getApplianceId(), ApplianceStatusResponse.newBuilder()
                        .setApplianceId(command.getApplianceId()).setApplianceName(command.getApplianceName())
                        .setStatus(command.getAction()).setPowerConsumption(power)
                        .setLastUpdated(timestamp).setMessage("Last command: " + command.getAction()).build());

                responseObserver.onNext(status);
                logger.info("Status sent: " + command.getApplianceName() + " -> " + command.getAction() + " | " + power + "W");
            }

            @Override
            public void onError(Throwable t) {
                // Client cancelled or disconnected — log and clean up
                logger.severe("ControlAppliance stream error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("Client closed command stream");
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getApplianceStatus(ApplianceStatusRequest request, StreamObserver<ApplianceStatusResponse> responseObserver) {
        logger.info("GetApplianceStatus: " + request.getApplianceId());
        try {
            if (Context.current().isCancelled()) {
                responseObserver.onError(Status.CANCELLED.withDescription("Request cancelled by client").asRuntimeException()); return;
            }
            if (request.getApplianceId().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("appliance_id cannot be empty").asRuntimeException()); return;
            }
            ApplianceStatusResponse stored = applianceStore.get(request.getApplianceId());
            if (stored == null) {
                responseObserver.onError(Status.NOT_FOUND.withDescription("No status for appliance: "
                        + request.getApplianceId() + ". Send a command first.").asRuntimeException()); return;
            }
            responseObserver.onNext(stored);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Error: " + e.getMessage()).asRuntimeException());
        }
    }

    private boolean isValidAction(String action) {
        return action.equals("ON") || action.equals("OFF") || action.equals("ECO");
    }

    private double calculatePower(String name, String action) {
        if (action.equals("OFF")) return 0.0;
        String n = name.toLowerCase();
        if (n.contains("air conditioner") || n.contains("ac")) return action.equals("ECO") ? 800.0 : 1500.0;
        if (n.contains("washing machine") || n.contains("washer")) return action.equals("ECO") ? 400.0 : 900.0;
        if (n.contains("light") || n.contains("lamp")) return action.equals("ECO") ? 5.0 : 10.0;
        if (n.contains("fan")) return action.equals("ECO") ? 20.0 : 50.0;
        if (n.contains("fridge") || n.contains("refrigerator")) return action.equals("ECO") ? 80.0 : 150.0;
        if (n.contains("dishwasher")) return action.equals("ECO") ? 700.0 : 1200.0;
        return action.equals("ECO") ? 100.0 : 200.0;
    }

    private String now() { return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); }
}