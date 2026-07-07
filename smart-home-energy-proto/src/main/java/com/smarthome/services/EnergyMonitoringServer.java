package com.smarthome.services;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
//import com.smarthome.util.AuthInterceptor;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * Energy Monitoring Server
 *
 * Starts the gRPC server and registers the service with jmDNS
 * so that the GUI client can discover it automatically.
 */
public class EnergyMonitoringServer {

    private static final Logger logger = Logger.getLogger(EnergyMonitoringServer.class.getName());

    // jmDNS service type — must match exactly what the client looks for
    public static final String SERVICE_TYPE = "_energymonitoring._tcp.local.";
    public static final String SERVICE_NAME = "EnergyMonitoringService";
    private static final int PORT = 50051;

    private Server grpcServer;
    private JmDNS jmdns;

    public void start() throws IOException, InterruptedException {

        // ── 1. Start the gRPC server ──────────────────────────────
        grpcServer = ServerBuilder.forPort(PORT)
                .addService(ServerInterceptors.intercept(
                        new EnergyMonitoringServiceImpl()))
                      //  new AuthInterceptor()))          // auth metadata interceptor
                .build()
                .start();

        logger.info("EnergyMonitoringServer started on port " + PORT);

        // ── 2. Register with jmDNS ───────────────────────────────
        jmdns = JmDNS.create(InetAddress.getLocalHost());

        ServiceInfo serviceInfo = ServiceInfo.create(
                SERVICE_TYPE,                            // service type
                SERVICE_NAME,                            // service name
                PORT,                                    // port
                "Smart Home Energy Monitoring Service"   // description
        );

        jmdns.registerService(serviceInfo);
        logger.info("Service registered with jmDNS: " + SERVICE_NAME + " on port " + PORT);

        // ── 3. Shutdown hook ─────────────────────────────────────
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down EnergyMonitoringServer...");
            stop();
        }));

        grpcServer.awaitTermination();
    }

    public void stop() {
        if (jmdns != null) {
            try {
                jmdns.unregisterAllServices();
                jmdns.close();
                logger.info("jmDNS services unregistered");
            } catch (IOException e) {
                logger.warning("Error closing jmDNS: " + e.getMessage());
            }
        }
        if (grpcServer != null) {
            grpcServer.shutdown();
            logger.info("gRPC server shut down");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        EnergyMonitoringServer server = new EnergyMonitoringServer();
        server.start();
    }
}
