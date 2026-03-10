package com.payment.publisher;

import com.payment.event.PaymentFailedEvent;
import com.payment.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {
    private final KafkaTemplate<String,Object> eventKafkaTemplate;
    public void publishSuccessPaymentEvent(PaymentSuccessEvent paymentSuccessEvent) {

        log.info("Sent Payment Success Event ");
        eventKafkaTemplate.send("PAYMENT_SUCCESS",paymentSuccessEvent);
    }

    public void publishFailedPaymentEventToOrderService(PaymentFailedEvent paymentFailedEvent) {

        log.info("Sent Payment Failure Event ");
        eventKafkaTemplate.send("PAYMENT_FAILED",paymentFailedEvent);
    }
}
