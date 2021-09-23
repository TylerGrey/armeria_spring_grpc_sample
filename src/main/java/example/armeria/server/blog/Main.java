package example.armeria.server.blog;

import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.HttpServiceWithRoutes;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.cors.CorsService;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.docs.DocServiceFilter;
import com.linecorp.armeria.server.grpc.GrpcService;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.reflection.v1alpha.ServerReflectionGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import example.armeria.server.blog.Hello.HelloRequest;

import java.util.function.Function;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        final Server server = newCompositeServer(8080, 8443);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop().join();
            logger.info("Server has been stopped.");
        }));

        server.start().join();

        logger.info("Server has been started. Serving DocService at http://127.0.0.1:{}/docs",
                server.activeLocalPort());
    }

    /**
     * Returns a new {@link Server} instance which serves the blog service.
     *
     * @param port the port that the server is to be bound to
     */
    static Server newServer(int port) {
        final ServerBuilder sb = Server.builder();
        final DocService docService =
                DocService.builder()
                        .exampleRequests(BlogService.class,
                                "createBlogPost",
                                "{\"title\":\"My first blog\", \"content\":\"Hello Armeria!\"}")
                        .build();
        return sb.http(port)
                .annotatedService(new BlogService())
                .serviceUnder("/docs", docService)
                .build();
    }

    private static Server newServer(int httpPort, int httpsPort) throws Exception {
        final ServerBuilder sb = Server.builder();
        sb.http(httpPort)
                .https(httpsPort)
                .tlsSelfSigned();
        configureServices(sb);
        return sb.build();
    }

    static void configureServices(ServerBuilder sb) {
        final HelloRequest exampleRequest = HelloRequest.newBuilder().setName("Armeria").build();
        final HttpServiceWithRoutes grpcService =
                GrpcService.builder()
                        .addService(new HelloServiceImpl())
                        // See https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md
                        .addService(ProtoReflectionService.newInstance())
                        .supportedSerializationFormats(GrpcSerializationFormats.values())
                        .enableUnframedRequests(true)
                        // You can set useBlockingTaskExecutor(true) in order to execute all gRPC
                        // methods in the blockingTaskExecutor thread pool.
                        // .useBlockingTaskExecutor(true)
                        .build();
        sb.service(grpcService)
//                .service("prefix:/prefix", grpcService)
                // You can access the documentation service at http://127.0.0.1:8080/docs.
                // See https://armeria.dev/docs/server-docservice for more information.
                .serviceUnder("/docs",
                        DocService.builder()
                                .exampleRequests(HelloServiceGrpc.SERVICE_NAME,
                                        "Hello", exampleRequest)
                                .exampleRequests(HelloServiceGrpc.SERVICE_NAME,
                                        "LazyHello", exampleRequest)
                                .exampleRequests(HelloServiceGrpc.SERVICE_NAME,
                                        "BlockingHello", exampleRequest)
                                .exclude(DocServiceFilter.ofServiceName(
                                        ServerReflectionGrpc.SERVICE_NAME))
                                .build());
    }

    private static Server newCompositeServer(int httpPort, int httpsPort) throws Exception {
        final HelloRequest exampleRequest = HelloRequest.newBuilder().setName("Armeria").build();

        final ServerBuilder sb = Server.builder();
        final DocService docService =
                DocService.builder()
                        .exampleRequests(BlogService.class,
                                "createBlogPost",
                                "{\"title\":\"My first blog\", \"content\":\"Hello Armeria!\"}")
                        .exampleRequests(HelloServiceGrpc.SERVICE_NAME,
                                "Hello", exampleRequest)
                        .exampleRequests(HelloServiceGrpc.SERVICE_NAME,
                                "LazyHello", exampleRequest)
                        .exampleRequests(HelloServiceGrpc.SERVICE_NAME,
                                "BlockingHello", exampleRequest)
                        .exclude(DocServiceFilter.ofServiceName(
                                ServerReflectionGrpc.SERVICE_NAME))
                        .build();

        final HttpServiceWithRoutes grpcService =
                GrpcService.builder()
                        .addService(new HelloServiceImpl())
                        // See https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md
                        .addService(ProtoReflectionService.newInstance())
                        .supportedSerializationFormats(GrpcSerializationFormats.values())
                        .enableUnframedRequests(true)
                        // You can set useBlockingTaskExecutor(true) in order to execute all gRPC
                        // methods in the blockingTaskExecutor thread pool.
                        // .useBlockingTaskExecutor(true)
                        .build();

        Function<? super HttpService, CorsService> corsService =
                CorsService.builderForAnyOrigin()
                        .allowCredentials()
                        .allowRequestMethods(HttpMethod.POST, HttpMethod.GET)
                        .allowRequestHeaders("allow_request_header")
                        .exposeHeaders("expose_header_1", "expose_header_2")
                        .preflightResponseHeader("x-preflight-cors", "Hello CORS")
                        .newDecorator();

        return sb.http(httpPort)
                .https(httpsPort)
                .annotatedService(new BlogService())
                .serviceUnder("/docs", docService)
                .decoratorUnder("/", corsService)
                .tlsSelfSigned()
                .service(grpcService)
                .build();
    }
}
