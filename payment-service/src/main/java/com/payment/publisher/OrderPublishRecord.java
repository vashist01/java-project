package com.payment.publisher;

public record OrderPublishRecord(String orderId, String uesrId, double amount) {
}
