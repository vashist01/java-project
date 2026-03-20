package com.payment.listener;

import com.payment.publisher.OrderPublishRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {


    @KafkaListener(topics = "ORDER_CREATED",groupId = "payment-group")
    public void handleOrderCreated(OrderPublishRecord orderPublishRecord){
        log.info("handleOrderCreated method is execute with ORDER_CREATED EVENT.");

    }

    @KafkaListener(topics = "ORDER_CREATED.DLT")
    public void handleDeadLatterTopic(ConsumerRecord<String,Objects> consumerRecord){
        log.error("DLT message: {}", consumerRecord.value());
        // alert, save to DB, manual investigation
    }

}
