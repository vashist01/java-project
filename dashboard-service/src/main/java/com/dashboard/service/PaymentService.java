package com.dashboard.service;

import com.dashboard.client.PaymentFeginClient;
import com.dashboard.dto.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentFeginClient paymentFeginClient;
    @CircuitBreaker(name = "paymentService", fallbackMethod = "fallback")
    @TimeLimiter(name = "paymentService")
    public CompletableFuture<PaymentResponse> getSafePayment() {
        return CompletableFuture.supplyAsync(() -> paymentFeginClient.getPaymentTransaction());
    }
}
