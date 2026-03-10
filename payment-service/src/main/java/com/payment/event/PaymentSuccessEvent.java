package com.payment.event;

public record PaymentSuccessEvent(String orderId,double amount,String paymentTransactionId) {
}
