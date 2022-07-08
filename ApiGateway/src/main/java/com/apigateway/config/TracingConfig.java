package com.apigateway.config;

import io.opentracing.Tracer;
import io.opentracing.contrib.grpc.TracingClientInterceptor;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {
    @GrpcGlobalClientInterceptor
    TracingClientInterceptor tracingInterceptor(Tracer tracer) {
        return TracingClientInterceptor
                .newBuilder()
                .withTracer(tracer)
                .build();
    }
}
