package com.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

@Component
@Slf4j
public class GatewayLoggingFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest httpRequest = exchange.getRequest();
        String path = httpRequest.getURI().getPath();
        String method =  httpRequest.getMethod().name();
        String requestId = exchange.getRequest().getId();
        log.info("Incoming Request -> id: {}, method: {}, path: {}",
                requestId, method, path);
        long requestStartTime = System.currentTimeMillis();

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse httpServerResponse =   exchange.getResponse();
            HttpStatusCode statusCode = httpServerResponse.getStatusCode();
            long responseDuration = System.currentTimeMillis()-requestStartTime;
            log.info("Outgoing Response -> id: {}, status: {}, duration: {} ms",
                    requestId,statusCode,responseDuration);
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
