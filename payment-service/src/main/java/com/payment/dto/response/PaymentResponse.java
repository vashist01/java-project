package com.payment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponse {

    private String transactionId;
    private String status;
    private String message;
    private Double amount;
    private String paymentUrl;
    private String orderId;
}