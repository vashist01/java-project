package com.dashboard.service;

import com.dashboard.client.RefundFeginClient;
import com.dashboard.dto.RefundResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class RefundService {
    private final RefundFeginClient refundFeginClient;
    @CircuitBreaker(name = "refundService",fallbackMethod = "fallback")
    @TimeLimiter(name = "refundService")
    @Retry(name = "paymentService",fallbackMethod = "fallback")
    public CompletableFuture<RefundResponse> getAllRefund() {
        return CompletableFuture.supplyAsync(() -> refundFeginClient.getAllRefund());
    }
}
