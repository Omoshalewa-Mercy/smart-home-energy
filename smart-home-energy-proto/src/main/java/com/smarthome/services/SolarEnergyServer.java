package com.smarthome.services;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import com.smarthome.util.AuthInterceptor;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * Solar Energy Server
 *
 * Starts the gRPC server on port 50052 and registers
 * the service with jmDNS for automatic discovery.
 */
public class SolarEnergyServer {

    private static final Logger logger = Logger.getLogger(SolarEnergyServer.class.getName());

    public static final String SERVICE_TYPE = "_solarenergy._tcp.local.";
    public static final String SERVICE_NAME = "SolarEnergyService";
    private static final int PORT = 50052;

    private Server grpcServer;
    private JmDNS jmdns;

    public void start() throws IOException, InterruptedException {

        // ?? 1. Start the gRPC server ??????????????????????????????
        grpcServer = ServerBuilder.forPort(PORT)
                .addService(ServerInterceptors.intercept(
                        new SolarEnergyServiceImpl(),
                        new AuthInterceptor()))
                .build()
                .start();

        logger.info("SolarEnergyServer started on port " + PORT);

        // ?? 2. Register with jmDNS ???????????????????????????????
        jmdns = JmDNS.create(InetAddress.getLocalHost());

        ServiceInfo serviceInfo = ServiceInfo.create(
                SERVICE_TYPE,
                SERVICE_NAME,
                PORT,
                "Smart Home Solar Energy Service"
        );

        jmdns.registerService(serviceInfo);
        logger.info("Service registered with jmDNS: " + SERVICE_NAME + " on port " + PORT);

        // ?? 3. Shutdown hook ?????????????????????????????????????
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down SolarEnergyServer...");
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
        SolarEnergyServer server = new SolarEnergyServer();
        server.start();
    }
}
