package com.dashboard.service;

import com.dashboard.client.OrderFeginClient;
import com.dashboard.client.PaymentFeginClient;
import com.dashboard.client.RefundFeginClient;
import com.dashboard.dto.DashboardResponse;
import com.dashboard.dto.OrderResponse;
import com.dashboard.dto.PaymentResponse;
import com.dashboard.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
    public class DashboardService {
    private PaymentService paymentService;
    private OrderService orderService;
    private RefundService refundService;
    private final Executor executors;
    public  DashboardResponse getDashboard() {

        CompletableFuture<PaymentResponse> paymentFuture =
                CompletableFuture.supplyAsync(() -> paymentService.getSafePayment(), executors)
                        .exceptionally(ex -> new PaymentResponse());

   CompletableFuture<OrderResponse>  orderResponseCompletableFuture =
           CompletableFuture.supplyAsync(() ->
                   orderService.getOrders(),executors).exceptionally(exception -> new OrderResponse()
                  );

   CompletableFuture<RefundResponse> refundResponseCompletableFuture = CompletableFuture.supplyAsync(() -> refundService.getAllRefund(),executors)
           .exceptionally(exception -> new RefundResponse());

   CompletableFuture<Void> allResponse = CompletableFuture.allOf(paymentFuture,orderResponseCompletableFuture,refundResponseCompletableFuture);

    return allResponse.thenApply(res -> {
        PaymentResponse paymentResponse = paymentFuture.join();
        OrderResponse orderResponse = orderResponseCompletableFuture.join();
        RefundResponse refundResponse = refundResponseCompletableFuture.join();

        return DashboardResponse.builder().orderResponseList(List.of(orderResponse)).
                paymentResponseList(List.of(paymentResponse)).
                refundResponseList(List.of(refundResponse)).build();
    }).join();
    }
}
