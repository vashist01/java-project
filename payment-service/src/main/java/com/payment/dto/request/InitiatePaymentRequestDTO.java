package com.payment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiatePaymentRequestDTO {

    private String orderId;
    private Double amount;
}