package com.smarthome.generated;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * ──────────────────────────────────────────────
 *  Service Definition
 * ──────────────────────────────────────────────
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: solar_energy.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SolarEnergyServiceGrpc {

  private SolarEnergyServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "solarenergy.SolarEnergyService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.smarthome.generated.SolarReading,
      com.smarthome.generated.SolarSummary> getUploadSolarReadingsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UploadSolarReadings",
      requestType = com.smarthome.generated.SolarReading.class,
      responseType = com.smarthome.generated.SolarSummary.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<com.smarthome.generated.SolarReading,
      com.smarthome.generated.SolarSummary> getUploadSolarReadingsMethod() {
    io.grpc.MethodDescriptor<com.smarthome.generated.SolarReading, com.smarthome.generated.SolarSummary> getUploadSolarReadingsMethod;
    if ((getUploadSolarReadingsMethod = SolarEnergyServiceGrpc.getUploadSolarReadingsMethod) == null) {
      synchronized (SolarEnergyServiceGrpc.class) {
        if ((getUploadSolarReadingsMethod = SolarEnergyServiceGrpc.getUploadSolarReadingsMethod) == null) {
          SolarEnergyServiceGrpc.getUploadSolarReadingsMethod = getUploadSolarReadingsMethod =
              io.grpc.MethodDescriptor.<com.smarthome.generated.SolarReading, com.smarthome.generated.SolarSummary>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UploadSolarReadings"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.SolarReading.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.SolarSummary.getDefaultInstance()))
              .setSchemaDescriptor(new SolarEnergyServiceMethodDescriptorSupplier("UploadSolarReadings"))
              .build();
        }
      }
    }
    return getUploadSolarReadingsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.smarthome.generated.DailySummaryRequest,
      com.smarthome.generated.DailySummaryResponse> getGetDailySummaryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetDailySummary",
      requestType = com.smarthome.generated.DailySummaryRequest.class,
      responseType = com.smarthome.generated.DailySummaryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.smarthome.generated.DailySummaryRequest,
      com.smarthome.generated.DailySummaryResponse> getGetDailySummaryMethod() {
    io.grpc.MethodDescriptor<com.smarthome.generated.DailySummaryRequest, com.smarthome.generated.DailySummaryResponse> getGetDailySummaryMethod;
    if ((getGetDailySummaryMethod = SolarEnergyServiceGrpc.getGetDailySummaryMethod) == null) {
      synchronized (SolarEnergyServiceGrpc.class) {
        if ((getGetDailySummaryMethod = SolarEnergyServiceGrpc.getGetDailySummaryMethod) == null) {
          SolarEnergyServiceGrpc.getGetDailySummaryMethod = getGetDailySummaryMethod =
              io.grpc.MethodDescriptor.<com.smarthome.generated.DailySummaryRequest, com.smarthome.generated.DailySummaryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetDailySummary"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.DailySummaryRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.DailySummaryResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SolarEnergyServiceMethodDescriptorSupplier("GetDailySummary"))
              .build();
        }
      }
    }
    return getGetDailySummaryMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SolarEnergyServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SolarEnergyServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SolarEnergyServiceStub>() {
        @java.lang.Override
        public SolarEnergyServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SolarEnergyServiceStub(channel, callOptions);
        }
      };
    return SolarEnergyServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SolarEnergyServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SolarEnergyServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SolarEnergyServiceBlockingStub>() {
        @java.lang.Override
        public SolarEnergyServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SolarEnergyServiceBlockingStub(channel, callOptions);
        }
      };
    return SolarEnergyServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SolarEnergyServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SolarEnergyServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SolarEnergyServiceFutureStub>() {
        @java.lang.Override
        public SolarEnergyServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SolarEnergyServiceFutureStub(channel, callOptions);
        }
      };
    return SolarEnergyServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Client Streaming RPC: client streams multiple hourly readings,
     * server returns a daily generation summary when the stream ends
     * </pre>
     */
    default io.grpc.stub.StreamObserver<com.smarthome.generated.SolarReading> uploadSolarReadings(
        io.grpc.stub.StreamObserver<com.smarthome.generated.SolarSummary> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getUploadSolarReadingsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Unary RPC: retrieve the stored daily summary for a specific date
     * </pre>
     */
    default void getDailySummary(com.smarthome.generated.DailySummaryRequest request,
        io.grpc.stub.StreamObserver<com.smarthome.generated.DailySummaryResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetDailySummaryMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service SolarEnergyService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static abstract class SolarEnergyServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SolarEnergyServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service SolarEnergyService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static final class SolarEnergyServiceStub
      extends io.grpc.stub.AbstractAsyncStub<SolarEnergyServiceStub> {
    private SolarEnergyServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SolarEnergyServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SolarEnergyServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Client Streaming RPC: client streams multiple hourly readings,
     * server returns a daily generation summary when the stream ends
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.smarthome.generated.SolarReading> uploadSolarReadings(
        io.grpc.stub.StreamObserver<com.smarthome.generated.SolarSummary> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getUploadSolarReadingsMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * Unary RPC: retrieve the stored daily summary for a specific date
     * </pre>
     */
    public void getDailySummary(com.smarthome.generated.DailySummaryRequest request,
        io.grpc.stub.StreamObserver<com.smarthome.generated.DailySummaryResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetDailySummaryMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service SolarEnergyService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static final class SolarEnergyServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SolarEnergyServiceBlockingStub> {
    private SolarEnergyServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SolarEnergyServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SolarEnergyServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Unary RPC: retrieve the stored daily summary for a specific date
     * </pre>
     */
    public com.smarthome.generated.DailySummaryResponse getDailySummary(com.smarthome.generated.DailySummaryRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetDailySummaryMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service SolarEnergyService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static final class SolarEnergyServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<SolarEnergyServiceFutureStub> {
    private SolarEnergyServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SolarEnergyServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SolarEnergyServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Unary RPC: retrieve the stored daily summary for a specific date
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smarthome.generated.DailySummaryResponse> getDailySummary(
        com.smarthome.generated.DailySummaryRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetDailySummaryMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_DAILY_SUMMARY = 0;
  private static final int METHODID_UPLOAD_SOLAR_READINGS = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_DAILY_SUMMARY:
          serviceImpl.getDailySummary((com.smarthome.generated.DailySummaryRequest) request,
              (io.grpc.stub.StreamObserver<com.smarthome.generated.DailySummaryResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_UPLOAD_SOLAR_READINGS:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.uploadSolarReadings(
              (io.grpc.stub.StreamObserver<com.smarthome.generated.SolarSummary>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getUploadSolarReadingsMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              com.smarthome.generated.SolarReading,
              com.smarthome.generated.SolarSummary>(
                service, METHODID_UPLOAD_SOLAR_READINGS)))
        .addMethod(
          getGetDailySummaryMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.smarthome.generated.DailySummaryRequest,
              com.smarthome.generated.DailySummaryResponse>(
                service, METHODID_GET_DAILY_SUMMARY)))
        .build();
  }

  private static abstract class SolarEnergyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SolarEnergyServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.smarthome.generated.SolarEnergyProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SolarEnergyService");
    }
  }

  private static final class SolarEnergyServiceFileDescriptorSupplier
      extends SolarEnergyServiceBaseDescriptorSupplier {
    SolarEnergyServiceFileDescriptorSupplier() {}
  }

  private static final class SolarEnergyServiceMethodDescriptorSupplier
      extends SolarEnergyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SolarEnergyServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SolarEnergyServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SolarEnergyServiceFileDescriptorSupplier())
              .addMethod(getUploadSolarReadingsMethod())
              .addMethod(getGetDailySummaryMethod())
              .build();
        }
      }
    }
    return result;
  }
}
