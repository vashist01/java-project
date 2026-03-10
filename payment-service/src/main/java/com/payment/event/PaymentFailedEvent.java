package com.payment.event;

public record PaymentFailedEvent(String orderId,String reason) {
}
