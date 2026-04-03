package com.dashboard.dto;

import lombok.Builder;

import java.util.List;
@Builder
public class DashboardResponse {
    private List<PaymentResponse> paymentResponseList;
    private List<RefundResponse> refundResponseList;
    private List<OrderResponse> orderResponseList;
}
