package com.payment.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEvent {

    private final KafkaTemplate<String,Object> kafkaTemplate;
    public void sendNotification(String event) {
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send("notification-event",event);
            return true;
        });
    }
}
