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
    comments = "Source: appliance_control.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ApplianceControlServiceGrpc {

  private ApplianceControlServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "appliancecontrol.ApplianceControlService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.smarthome.generated.ApplianceCommand,
      com.smarthome.generated.ApplianceStatus> getControlApplianceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ControlAppliance",
      requestType = com.smarthome.generated.ApplianceCommand.class,
      responseType = com.smarthome.generated.ApplianceStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.smarthome.generated.ApplianceCommand,
      com.smarthome.generated.ApplianceStatus> getControlApplianceMethod() {
    io.grpc.MethodDescriptor<com.smarthome.generated.ApplianceCommand, com.smarthome.generated.ApplianceStatus> getControlApplianceMethod;
    if ((getControlApplianceMethod = ApplianceControlServiceGrpc.getControlApplianceMethod) == null) {
      synchronized (ApplianceControlServiceGrpc.class) {
        if ((getControlApplianceMethod = ApplianceControlServiceGrpc.getControlApplianceMethod) == null) {
          ApplianceControlServiceGrpc.getControlApplianceMethod = getControlApplianceMethod =
              io.grpc.MethodDescriptor.<com.smarthome.generated.ApplianceCommand, com.smarthome.generated.ApplianceStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ControlAppliance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.ApplianceCommand.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.ApplianceStatus.getDefaultInstance()))
              .setSchemaDescriptor(new ApplianceControlServiceMethodDescriptorSupplier("ControlAppliance"))
              .build();
        }
      }
    }
    return getControlApplianceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.smarthome.generated.ApplianceStatusRequest,
      com.smarthome.generated.ApplianceStatusResponse> getGetApplianceStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetApplianceStatus",
      requestType = com.smarthome.generated.ApplianceStatusRequest.class,
      responseType = com.smarthome.generated.ApplianceStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.smarthome.generated.ApplianceStatusRequest,
      com.smarthome.generated.ApplianceStatusResponse> getGetApplianceStatusMethod() {
    io.grpc.MethodDescriptor<com.smarthome.generated.ApplianceStatusRequest, com.smarthome.generated.ApplianceStatusResponse> getGetApplianceStatusMethod;
    if ((getGetApplianceStatusMethod = ApplianceControlServiceGrpc.getGetApplianceStatusMethod) == null) {
      synchronized (ApplianceControlServiceGrpc.class) {
        if ((getGetApplianceStatusMethod = ApplianceControlServiceGrpc.getGetApplianceStatusMethod) == null) {
          ApplianceControlServiceGrpc.getGetApplianceStatusMethod = getGetApplianceStatusMethod =
              io.grpc.MethodDescriptor.<com.smarthome.generated.ApplianceStatusRequest, com.smarthome.generated.ApplianceStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetApplianceStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.ApplianceStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smarthome.generated.ApplianceStatusResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ApplianceControlServiceMethodDescriptorSupplier("GetApplianceStatus"))
              .build();
        }
      }
    }
    return getGetApplianceStatusMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ApplianceControlServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ApplianceControlServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ApplianceControlServiceStub>() {
        @java.lang.Override
        public ApplianceControlServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ApplianceControlServiceStub(channel, callOptions);
        }
      };
    return ApplianceControlServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ApplianceControlServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ApplianceControlServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ApplianceControlServiceBlockingStub>() {
        @java.lang.Override
        public ApplianceControlServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ApplianceControlServiceBlockingStub(channel, callOptions);
        }
      };
    return ApplianceControlServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ApplianceControlServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ApplianceControlServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ApplianceControlServiceFutureStub>() {
        @java.lang.Override
        public ApplianceControlServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ApplianceControlServiceFutureStub(channel, callOptions);
        }
      };
    return ApplianceControlServiceFutureStub.newStub(factory, channel);
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
     * Bidirectional Streaming RPC: client sends appliance commands,
     * server streams back live status updates simultaneously
     * </pre>
     */
    default io.grpc.stub.StreamObserver<com.smarthome.generated.ApplianceCommand> controlAppliance(
        io.grpc.stub.StreamObserver<com.smarthome.generated.ApplianceStatus> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getControlApplianceMethod(), responseObserver);
    }

    /**
     * <pre>
     * Unary RPC: get the current status of a specific appliance
     * </pre>
     */
    default void getApplianceStatus(com.smarthome.generated.ApplianceStatusRequest request,
        io.grpc.stub.StreamObserver<com.smarthome.generated.ApplianceStatusResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetApplianceStatusMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ApplianceControlService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static abstract class ApplianceControlServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ApplianceControlServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ApplianceControlService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static final class ApplianceControlServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ApplianceControlServiceStub> {
    private ApplianceControlServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ApplianceControlServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ApplianceControlServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Bidirectional Streaming RPC: client sends appliance commands,
     * server streams back live status updates simultaneously
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.smarthome.generated.ApplianceCommand> controlAppliance(
        io.grpc.stub.StreamObserver<com.smarthome.generated.ApplianceStatus> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getControlApplianceMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * Unary RPC: get the current status of a specific appliance
     * </pre>
     */
    public void getApplianceStatus(com.smarthome.generated.ApplianceStatusRequest request,
        io.grpc.stub.StreamObserver<com.smarthome.generated.ApplianceStatusResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetApplianceStatusMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ApplianceControlService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static final class ApplianceControlServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ApplianceControlServiceBlockingStub> {
    private ApplianceControlServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ApplianceControlServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ApplianceControlServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Unary RPC: get the current status of a specific appliance
     * </pre>
     */
    public com.smarthome.generated.ApplianceStatusResponse getApplianceStatus(com.smarthome.generated.ApplianceStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetApplianceStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ApplianceControlService.
   * <pre>
   * ──────────────────────────────────────────────
   *  Service Definition
   * ──────────────────────────────────────────────
   * </pre>
   */
  public static final class ApplianceControlServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ApplianceControlServiceFutureStub> {
    private ApplianceControlServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ApplianceControlServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ApplianceControlServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Unary RPC: get the current status of a specific appliance
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smarthome.generated.ApplianceStatusResponse> getApplianceStatus(
        com.smarthome.generated.ApplianceStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetApplianceStatusMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_APPLIANCE_STATUS = 0;
  private static final int METHODID_CONTROL_APPLIANCE = 1;

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
        case METHODID_GET_APPLIANCE_STATUS:
          serviceImpl.getApplianceStatus((com.smarthome.generated.ApplianceStatusRequest) request,
              (io.grpc.stub.StreamObserver<com.smarthome.generated.ApplianceStatusResponse>) responseObserver);
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
        case METHODID_CONTROL_APPLIANCE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.controlAppliance(
              (io.grpc.stub.StreamObserver<com.smarthome.generated.ApplianceStatus>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getControlApplianceMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.smarthome.generated.ApplianceCommand,
              com.smarthome.generated.ApplianceStatus>(
                service, METHODID_CONTROL_APPLIANCE)))
        .addMethod(
          getGetApplianceStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.smarthome.generated.ApplianceStatusRequest,
              com.smarthome.generated.ApplianceStatusResponse>(
                service, METHODID_GET_APPLIANCE_STATUS)))
        .build();
  }

  private static abstract class ApplianceControlServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ApplianceControlServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.smarthome.generated.ApplianceControlProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ApplianceControlService");
    }
  }

  private static final class ApplianceControlServiceFileDescriptorSupplier
      extends ApplianceControlServiceBaseDescriptorSupplier {
    ApplianceControlServiceFileDescriptorSupplier() {}
  }

  private static final class ApplianceControlServiceMethodDescriptorSupplier
      extends ApplianceControlServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ApplianceControlServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ApplianceControlServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ApplianceControlServiceFileDescriptorSupplier())
              .addMethod(getControlApplianceMethod())
              .addMethod(getGetApplianceStatusMethod())
              .build();
        }
      }
    }
    return result;
  }
}
