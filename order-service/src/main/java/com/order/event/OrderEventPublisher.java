package com.order.event;

import com.order.model.OrderPublishRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record OrderEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {

   public void publishOrderCreated(OrderPublishRecord orderPublishRecord) {

        log.info("Info: send Order Event with order id : {}",orderPublishRecord);
        kafkaTemplate.send("order-crated",orderPublishRecord);
    }
}
