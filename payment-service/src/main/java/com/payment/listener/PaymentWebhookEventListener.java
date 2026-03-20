package com.payment.listener;

import com.payment.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentWebhookEventListener {
    private final PaymentTransactionService paymentTransactionService;
     private final NotificationEvent notificationEvent;
    @KafkaListener(topics="payment-webhook-event", containerFactory = "kafkaListenerContainerFactory")
    @RetryableTopic(attempts = "3", backoff =
    @Backoff(delay = 2000,multiplier = 2),
            dltTopicSuffix = "-dlq") // if event is failed then retry with 3 attempts ,
    // again failed 3 attempts event are in DLQ
    public void paymentWebhookListener(@Payload String webhookPayload, Acknowledgment acknowledgements){
        try{

            paymentTransactionService.processPaymentTransaction(webhookPayload);
            acknowledgements.acknowledge();
        }catch (Exception exception){
            throw exception;
        }
    }

    @KafkaListener(topics = "payment-webhook-event-dlq")
    public void handleDLQ(@Payload String event) {
       paymentTransactionService.processFailedTransaction(event);
       log.error("Stored DLQ event");
      notificationEvent.sendNotification(event);
    }
}
