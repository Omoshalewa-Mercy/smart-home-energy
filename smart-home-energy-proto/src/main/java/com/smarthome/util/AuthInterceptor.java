package com.smarthome.util;

import io.grpc.*;
import java.util.logging.Logger;

/**
 * AuthInterceptor
 *
 * A server-side gRPC interceptor that checks every incoming call
 * for a valid API key in the request metadata.
 *
 * This satisfies the "Metadata / Authentication" advanced gRPC requirement.
 *
 * The client must send the header:  x-api-key: smarthome-secret-2026
 */
public class AuthInterceptor implements ServerInterceptor {

    private static final Logger logger = Logger.getLogger(AuthInterceptor.class.getName());

    // Metadata key the client must include in every request
    public static final Metadata.Key<String> API_KEY_HEADER =
            Metadata.Key.of("x-api-key", Metadata.ASCII_STRING_MARSHALLER);

    // The expected API key value
    private static final String VALID_API_KEY = "smarthome-secret-2026";

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String apiKey = headers.get(API_KEY_HEADER);

        logger.info("Intercepted call to: " + call.getMethodDescriptor().getFullMethodName()
                + " | API key present: " + (apiKey != null));

        if (apiKey == null || !apiKey.equals(VALID_API_KEY)) {
            logger.warning("Rejected call â€” invalid or missing API key");
            call.close(
                Status.UNAUTHENTICATED.withDescription("Invalid or missing API key"),
                new Metadata()
            );
            // Return a no-op listener since the call is already closed
            return new ServerCall.Listener<ReqT>() {};
        }

        logger.info("API key accepted for: " + call.getMethodDescriptor().getFullMethodName());
        return next.startCall(call, headers);
    }
}
