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
    comments = "Source: energy_monitoring.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class EnergyMonitoringServiceGrpc {

  private EnergyMonitoringServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "energymonitoring.EnergyMonitoringService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.smarthome.generated.RecordUsageRequest,
      com.smarthome.generated.RecordUsageResponse> getRecordEnergyUsageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RecordEnergyUsage",
      requestType = com.smarthome.generated.RecordUsageRequest.class,
      responseType = com.smarthome.generated.RecordUsageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.smarthome.generated.RecordUsageRequest,
      com.smarthome.generated.RecordUsageResponse> getRecordEnergyUsageMethod() {
    io.grpc.MethodDescriptor<com.smarthome.generated.RecordUsageRequest, com.smarthome.generated.RecordUsageResponse> getRecordEnergyUsageMethod;
    if ((getRecordEnergyUsageMethod = EnergyMonitoringServiceGrpc.getRecordEnergyUsageMethod) == null) {
      synchronized (EnergyMonitoringServiceGrpc.class) {
        if ((getRecordEnergyUsageMethod = EnergyMonitoringServiceGrpc.getRecordEnergyUsageMethod) == null) {
          EnergyMonitoringServiceGrpc.getRecordEnergyUsageMethod = getRecordEnergyUsageMethod =
              io.grpc.MethodDescriptor.<com.smarthome.generated.RecordUsageRequest, com.smarthome.generated.RecordUsageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RecordEnergyUsage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.RecordUsageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.RecordUsageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EnergyMonitoringServiceMethodDescriptorSupplier("RecordEnergyUsage"))
              .build();
        }
      }
    }
    return getRecordEnergyUsageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.smarthome.generated.LatestReadingRequest,
      com.smarthome.generated.LatestReadingResponse> getGetLatestReadingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetLatestReading",
      requestType = com.smarthome.generated.LatestReadingRequest.class,
      responseType = com.smarthome.generated.LatestReadingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.smarthome.generated.LatestReadingRequest,
      com.smarthome.generated.LatestReadingResponse> getGetLatestReadingMethod() {
    io.grpc.MethodDescriptor<com.smarthome.generated.LatestReadingRequest, com.smarthome.generated.LatestReadingResponse> getGetLatestReadingMethod;
    if ((getGetLatestReadingMethod = EnergyMonitoringServiceGrpc.getGetLatestReadingMethod) == null) {
      synchronized (EnergyMonitoringServiceGrpc.class) {
        if ((getGetLatestReadingMethod = EnergyMonitoringServiceGrpc.getGetLatestReadingMethod) == null) {
          EnergyMonitoringServiceGrpc.getGetLatestReadingMethod = getGetLatestReadingMethod =
              io.grpc.MethodDescriptor.<com.smarthome.generated.LatestReadingRequest, com.smarthome.generated.LatestReadingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetLatestReading"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.LatestReadingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.LatestReadingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EnergyMonitoringServiceMethodDescriptorSupplier("GetLatestReading"))
              .build();
        }
      }
    }
    return getGetLatestReadingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.smarthome.generated.StreamRequest,
      com.smarthome.generated.EnergyReading> getStreamLiveReadingsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamLiveReadings",
      requestType = com.smarthome.generated.StreamRequest.class,
      responseType = com.smarthome.generated.EnergyReading.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.smarthome.generated.StreamRequest,
      com.smarthome.generated.EnergyReading> getStreamLiveReadingsMethod() {
    io.grpc.MethodDescriptor<com.smarthome.generated.StreamRequest, com.smarthome.generated.EnergyReading> getStreamLiveReadingsMethod;
    if ((getStreamLiveReadingsMethod = EnergyMonitoringServiceGrpc.getStreamLiveReadingsMethod) == null) {
      synchronized (EnergyMonitoringServiceGrpc.class) {
        if ((getStreamLiveReadingsMethod = EnergyMonitoringServiceGrpc.getStreamLiveReadingsMethod) == null) {
          EnergyMonitoringServiceGrpc.getStreamLiveReadingsMethod = getStreamLiveReadingsMethod =
              io.grpc.MethodDescriptor.<com.smarthome.generated.StreamRequest, com.smarthome.generated.EnergyReading>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamLiveReadings"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.StreamRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.EnergyReading.getDefaultInstance()))
              .setSchemaDescriptor(new EnergyMonitoringServiceMethodDescriptorSupplier("StreamLiveReadings"))
              .build();
        }
      }
    }
    return getStreamLiveReadingsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EnergyMonitoringServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EnergyMonitoringServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EnergyMonitoringServiceStub>() {
        @java.lang.Override
        public EnergyMonitoringServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EnergyMonitoringServiceStub(channel, callOptions);
        }
      };
    return EnergyMonitoringServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EnergyMonitoringServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EnergyMonitoringServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EnergyMonitoringServiceBlockingStub>() {
        @java.lang.Override
        public EnergyMonitoringServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EnergyMonitoringServiceBlockingStub(channel, callOptions);
        }
      };
    return EnergyMonitoringServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EnergyMonitoringServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EnergyMonitoringServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EnergyMonitoringServiceFutureStub>() {
        @java.lang.Override
        public EnergyMonitoringServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EnergyMonitoringServiceFutureStub(channel, callOptions);
        }
      };
    return EnergyMonitoringServiceFutureStub.newStub(factory, channel);
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
     * Unary RPC: record a single energy usage reading
     * </pre>
     */
    default void recordEnergyUsage(com.smarthome.generated.RecordUsageRequest request,
        io.grpc.stub.StreamObserver<com.smarthome.generated.RecordUsageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRecordEnergyUsageMethod(), responseObserver);
    }

    /**
     * <pre>
     * Unary RPC: get the most recent reading for a household
     * </pre>
     */
    default void getLatestReading(com.smarthome.generated.LatestReadingRequest request,
        io.grpc.stub.StreamObserver<com.smarthome.generated.LatestReadingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetLatestReadingMethod(), responseObserver);
    }

    /**
     * <pre>
     * Server Streaming RPC: server pushes live readings continuously to the client
     * </pre>
     */
    default void streamLiveReadings(com.smarthome.generated.StreamRequest request,
        io.grpc.stub.StreamObserver<com.smarthome.generated.EnergyReading> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStreamLiveReadingsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service EnergyMonitoringService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static abstract class EnergyMonitoringServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return EnergyMonitoringServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service EnergyMonitoringService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static final class EnergyMonitoringServiceStub
      extends io.grpc.stub.AbstractAsyncStub<EnergyMonitoringServiceStub> {
    private EnergyMonitoringServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EnergyMonitoringServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EnergyMonitoringServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Unary RPC: record a single energy usage reading
     * </pre>
     */
    public void recordEnergyUsage(com.smarthome.generated.RecordUsageRequest request,
        io.grpc.stub.StreamObserver<com.smarthome.generated.RecordUsageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRecordEnergyUsageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Unary RPC: get the most recent reading for a household
     * </pre>
     */
    public void getLatestReading(com.smarthome.generated.LatestReadingRequest request,
        io.grpc.stub.StreamObserver<com.smarthome.generated.LatestReadingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetLatestReadingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Server Streaming RPC: server pushes live readings continuously to the client
     * </pre>
     */
    public void streamLiveReadings(com.smarthome.generated.StreamRequest request,
        io.grpc.stub.StreamObserver<com.smarthome.generated.EnergyReading> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getStreamLiveReadingsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service EnergyMonitoringService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static final class EnergyMonitoringServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<EnergyMonitoringServiceBlockingStub> {
    private EnergyMonitoringServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EnergyMonitoringServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EnergyMonitoringServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Unary RPC: record a single energy usage reading
     * </pre>
     */
    public com.smarthome.generated.RecordUsageResponse recordEnergyUsage(com.smarthome.generated.RecordUsageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRecordEnergyUsageMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Unary RPC: get the most recent reading for a household
     * </pre>
     */
    public com.smarthome.generated.LatestReadingResponse getLatestReading(com.smarthome.generated.LatestReadingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetLatestReadingMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Server Streaming RPC: server pushes live readings continuously to the client
     * </pre>
     */
    public java.util.Iterator<com.smarthome.generated.EnergyReading> streamLiveReadings(
        com.smarthome.generated.StreamRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getStreamLiveReadingsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service EnergyMonitoringService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static final class EnergyMonitoringServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<EnergyMonitoringServiceFutureStub> {
    private EnergyMonitoringServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EnergyMonitoringServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EnergyMonitoringServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Unary RPC: record a single energy usage reading
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smarthome.generated.RecordUsageResponse> recordEnergyUsage(
        com.smarthome.generated.RecordUsageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRecordEnergyUsageMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Unary RPC: get the most recent reading for a household
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smarthome.generated.LatestReadingResponse> getLatestReading(
        com.smarthome.generated.LatestReadingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetLatestReadingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RECORD_ENERGY_USAGE = 0;
  private static final int METHODID_GET_LATEST_READING = 1;
  private static final int METHODID_STREAM_LIVE_READINGS = 2;

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
        case METHODID_RECORD_ENERGY_USAGE:
          serviceImpl.recordEnergyUsage((com.smarthome.generated.RecordUsageRequest) request,
              (io.grpc.stub.StreamObserver<com.smarthome.generated.RecordUsageResponse>) responseObserver);
          break;
        case METHODID_GET_LATEST_READING:
          serviceImpl.getLatestReading((com.smarthome.generated.LatestReadingRequest) request,
              (io.grpc.stub.StreamObserver<com.smarthome.generated.LatestReadingResponse>) responseObserver);
          break;
        case METHODID_STREAM_LIVE_READINGS:
          serviceImpl.streamLiveReadings((com.smarthome.generated.StreamRequest) request,
              (io.grpc.stub.StreamObserver<com.smarthome.generated.EnergyReading>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getRecordEnergyUsageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.smarthome.generated.RecordUsageRequest,
              com.smarthome.generated.RecordUsageResponse>(
                service, METHODID_RECORD_ENERGY_USAGE)))
        .addMethod(
          getGetLatestReadingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.smarthome.generated.LatestReadingRequest,
              com.smarthome.generated.LatestReadingResponse>(
                service, METHODID_GET_LATEST_READING)))
        .addMethod(
          getStreamLiveReadingsMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.smarthome.generated.StreamRequest,
              com.smarthome.generated.EnergyReading>(
                service, METHODID_STREAM_LIVE_READINGS)))
        .build();
  }

  private static abstract class EnergyMonitoringServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EnergyMonitoringServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.smarthome.generated.EnergyMonitoringProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("EnergyMonitoringService");
    }
  }

  private static final class EnergyMonitoringServiceFileDescriptorSupplier
      extends EnergyMonitoringServiceBaseDescriptorSupplier {
    EnergyMonitoringServiceFileDescriptorSupplier() {}
  }

  private static final class EnergyMonitoringServiceMethodDescriptorSupplier
      extends EnergyMonitoringServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    EnergyMonitoringServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (EnergyMonitoringServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EnergyMonitoringServiceFileDescriptorSupplier())
              .addMethod(getRecordEnergyUsageMethod())
              .addMethod(getGetLatestReadingMethod())
              .addMethod(getStreamLiveReadingsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
