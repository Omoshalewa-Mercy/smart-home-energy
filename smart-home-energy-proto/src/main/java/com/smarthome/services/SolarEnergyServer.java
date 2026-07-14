package com.smarthome.services;
import io.grpc.*;
import com.smarthome.util.AuthInterceptor;
import javax.jmdns.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

public class SolarEnergyServer {
    private static final Logger logger = Logger.getLogger(SolarEnergyServer.class.getName());
    public static final String SERVICE_TYPE = "_solarenergy._tcp.local.";
    public static final String SERVICE_NAME = "SolarEnergyService";
    private static final int PORT = 50052;
    private Server grpcServer;
    private JmDNS jmdns;

    public void start() throws IOException, InterruptedException {
        grpcServer = ServerBuilder.forPort(PORT)
                .addService(ServerInterceptors.intercept(new SolarEnergyServiceImpl(), new AuthInterceptor()))
                .build().start();
        logger.info("SolarEnergyServer started on port " + PORT);
        jmdns = JmDNS.create(InetAddress.getLocalHost());
        jmdns.registerService(ServiceInfo.create(SERVICE_TYPE, SERVICE_NAME, PORT, "Smart Home Solar Energy Service"));
        logger.info("Registered with jmDNS: " + SERVICE_NAME);
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        grpcServer.awaitTermination();
    }

    public void stop() {
        try { if (jmdns != null) { jmdns.unregisterAllServices(); jmdns.close(); } } catch (IOException e) { logger.warning(e.getMessage()); }
        if (grpcServer != null) grpcServer.shutdown();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new SolarEnergyServer().start();
    }
}
