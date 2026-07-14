package com.smarthome.util;

import io.grpc.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * DeadlineInterceptor
 *
 * A server-side interceptor that enforces a maximum deadline on all incoming
 * calls. If a client does not set a deadline, the server applies a default one.
 * If the remaining time on an existing deadline is less than the minimum allowed,
 * the call is rejected immediately with DEADLINE_EXCEEDED.
 *
 * This satisfies the "Deadlines" advanced gRPC requirement.
 */
public class DeadlineInterceptor implements ServerInterceptor {

    private static final Logger logger = Logger.getLogger(DeadlineInterceptor.class.getName());

    // Maximum time any single RPC call is allowed to run (30 seconds)
    private static final long MAX_DEADLINE_SECONDS = 30;

    // Minimum remaining time — calls with less than this remaining are rejected
    private static final long MIN_REMAINING_MS = 100;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        Context currentContext = Context.current();
        String methodName = call.getMethodDescriptor().getFullMethodName();

        // Check if the context already has a deadline set by the client
        if (currentContext.getDeadline() != null) {
            long remainingMs = currentContext.getDeadline().timeRemaining(TimeUnit.MILLISECONDS);
            logger.info("Call to " + methodName + " has deadline — remaining: " + remainingMs + "ms");

            // Reject if deadline has already expired or is about to
            if (remainingMs < MIN_REMAINING_MS) {
                logger.warning("Rejecting call to " + methodName + " — deadline already exceeded");
                call.close(
                    Status.DEADLINE_EXCEEDED.withDescription(
                            "Deadline exceeded before processing started for: " + methodName),
                    new Metadata()
                );
                return new ServerCall.Listener<ReqT>() {};
            }
        } else {
            // No deadline set by client — apply a server-side default
            logger.info("No deadline set for " + methodName + " — applying default of " + MAX_DEADLINE_SECONDS + "s");
            Context withDeadline = currentContext.withDeadlineAfter(MAX_DEADLINE_SECONDS, TimeUnit.SECONDS,
                    java.util.concurrent.Executors.newSingleThreadScheduledExecutor());
            return Contexts.interceptCall(withDeadline, call, headers, next);
        }

        return next.startCall(call, headers);
    }
}
