package com.payment.webhook.config;

import com.payment.webhook.dto.PaymentEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
    public static final String PAYMENT_TOPIC = "payment-webhook-event";
    public static final String PAYMENT_RETRY_TOPIC = "payment-webhook-event-retry";
    public static final String PAYMENT_DLQ_TOPIC = "payment-webhook-event-dlq";
    @Bean
    public NewTopic processPaymentTopic(){
        return TopicBuilder.name(PAYMENT_TOPIC).
                partitions(100).
        config("main.insync.replicas","2").
                replicas(3).build();

        //main.insync.replicas : ISR -> replica that fully sync with leader
        //ISR both replica have same data with leader(अगर दोनों replicas leader के साथ same data तक पहुँच चुके हैं:)
        //why ISR : replica is not enough suppose replica13 is slow and other replica is crash then r3 don't have data
        //data will lost , replica kaise data rok sakta hai , if r3 slow hai to to bo r3 ko hta deta hai uske pass
        // ab r1,r2 hai , ager r1 crash ho jata hai to r2 leader ban jata hai data lost nhi hot but
        // kafka isr ko automaticall update karta recover karta hai


        // replicas 3 is good in production means if we have 1 replica and if the broker hosting partition is crash
        // we lost the data , if replica(3) that means your data 3 copy , kafka will store copies in 3 broker
    }


    @Bean
    public NewTopic paymentWebhookRetryTopic(){

        return TopicBuilder.name(PAYMENT_RETRY_TOPIC).partitions(50)
                .replicas(3).
                config("retention.ms","43200000").build(); // 12

    }
    @Bean
    public NewTopic paymentWebhookDLQTopic(){
        return TopicBuilder.name(PAYMENT_DLQ_TOPIC).partitions(10).replicas(3)
                .config("retention.ms", "604800000") // 7 days
                .build();
    }

    @Bean
    public ProducerFactory<String, PaymentEvent> producerFactory() {

        Map<String, Object> configs = new HashMap<>();

        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // 🔥 REQUIRED FOR EXACTLY-ONCE
        configs.put(ProducerConfig.ACKS_CONFIG, "all");
        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configs.put(ProducerConfig.RETRIES_CONFIG, 3);
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

        DefaultKafkaProducerFactory<String, PaymentEvent> factory =
                new DefaultKafkaProducerFactory<>(configs);

        // 🔥 MUST for transactions
        factory.setTransactionIdPrefix("payment-tx-");

        return factory;
    }
    @Bean
    public KafkaTemplate<String, PaymentEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
