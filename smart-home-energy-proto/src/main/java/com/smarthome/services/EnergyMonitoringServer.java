package com.smarthome.services;
import io.grpc.*;
import io.grpc.ServerInterceptors;
import com.smarthome.util.AuthInterceptor;
import javax.jmdns.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

public class EnergyMonitoringServer {
    private static final Logger logger = Logger.getLogger(EnergyMonitoringServer.class.getName());
    public static final String SERVICE_TYPE = "_energymonitoring._tcp.local.";
    public static final String SERVICE_NAME = "EnergyMonitoringService";
    private static final int PORT = 50051;
    private Server grpcServer;
    private JmDNS jmdns;

    public void start() throws IOException, InterruptedException {
        grpcServer = ServerBuilder.forPort(PORT)
                .addService(ServerInterceptors.intercept(new EnergyMonitoringServiceImpl(), new AuthInterceptor()))
                .build().start();
        logger.info("EnergyMonitoringServer started on port " + PORT);
        jmdns = JmDNS.create(InetAddress.getLocalHost());
        jmdns.registerService(ServiceInfo.create(SERVICE_TYPE, SERVICE_NAME, PORT, "Smart Home Energy Monitoring Service"));
        logger.info("Registered with jmDNS: " + SERVICE_NAME);
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        grpcServer.awaitTermination();
    }

    public void stop() {
        try { if (jmdns != null) { jmdns.unregisterAllServices(); jmdns.close(); } } catch (IOException e) { logger.warning(e.getMessage()); }
        if (grpcServer != null) grpcServer.shutdown();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new EnergyMonitoringServer().start();
    }
}
