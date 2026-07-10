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
 * Smart Appliance Control Server
 *
 * Starts the gRPC server on port 50053 and registers
 * the service with jmDNS for automatic discovery.
 */
public class ApplianceControlServer {

    private static final Logger logger = Logger.getLogger(ApplianceControlServer.class.getName());

    public static final String SERVICE_TYPE = "_appliancecontrol._tcp.local.";
    public static final String SERVICE_NAME = "ApplianceControlService";
    private static final int PORT = 50053;

    private Server grpcServer;
    private JmDNS jmdns;

    public void start() throws IOException, InterruptedException {

        // ?? 1. Start the gRPC server ??????????????????????????????
        grpcServer = ServerBuilder.forPort(PORT)
                .addService(ServerInterceptors.intercept(
                        new ApplianceControlServiceImpl().bindService(),
                        new AuthInterceptor()))
                .build()
                .start();

        logger.info("ApplianceControlServer started on port " + PORT);

        // ?? 2. Register with jmDNS ???????????????????????????????
        jmdns = JmDNS.create(InetAddress.getLocalHost());

        ServiceInfo serviceInfo = ServiceInfo.create(
                SERVICE_TYPE,
                SERVICE_NAME,
                PORT,
                "Smart Home Appliance Control Service"
        );

        jmdns.registerService(serviceInfo);
        logger.info("Service registered with jmDNS: " + SERVICE_NAME + " on port " + PORT);

        // ?? 3. Shutdown hook ?????????????????????????????????????
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down ApplianceControlServer...");
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
        ApplianceControlServer server = new ApplianceControlServer();
        server.start();
    }
}
