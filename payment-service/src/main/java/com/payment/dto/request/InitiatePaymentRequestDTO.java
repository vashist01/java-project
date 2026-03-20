package com.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InitiatePaymentRequestDTO(
      @JsonProperty("order_id")
      String orderId,
      Double amount ){
}