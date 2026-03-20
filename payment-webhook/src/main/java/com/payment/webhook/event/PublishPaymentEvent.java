package com.payment.webhook.event;

import com.payment.webhook.dto.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishPaymentEvent {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void processPayment(String webHookRequest) {
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send("payment-webhook-event", new PaymentEvent(webHookRequest));
            return true;
        });
    }
}

