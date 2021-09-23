package example.armeria.server.blog;

import com.linecorp.armeria.common.grpc.GrpcSerializationFormats;
import com.linecorp.armeria.server.HttpServiceWithRoutes;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.docs.DocServiceFilter;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.reflection.v1alpha.ServerReflectionGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlogConfiguration {

    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(BlogService service) {
        return builder -> {
            final Hello.HelloRequest exampleRequest = Hello.HelloRequest.newBuilder().setName("Armeria").build();
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
            builder.service(grpcService);
            builder.serviceUnder("/docs", docService);
            builder.annotatedService(service);
            builder.tlsSelfSigned();
        };
    }
}
