package com.dashboard.service;

import com.dashboard.client.OrderFeginClient;
import com.dashboard.dto.OrderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderFeginClient orderFeginClient;
    @CircuitBreaker(name = "orderService",fallbackMethod = "fallback")
    @TimeLimiter(name = "orderService")
    public CompletableFuture<OrderResponse> getOrders() {
        return CompletableFuture.supplyAsync(() -> orderFeginClient.getOrders());
    }
}
