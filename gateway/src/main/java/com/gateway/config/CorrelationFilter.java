package com.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Component
public class CorrelationFilter implements GlobalFilter {
    private static final String CORRELATION_ID = "X-Correlation-Id";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //Now every service logs same request id.
        String correlationId = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(CORRELATION_ID)).orElse(UUID.randomUUID().toString());
        exchange.getRequest().mutate().header(CORRELATION_ID,correlationId).build();

        return chain.filter(exchange);
    }
}
