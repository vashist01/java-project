package com.order.service;

import com.order.entity.OrderDocument;
import com.order.event.OrderEventPublisher;
import com.order.model.OrderPublishRecord;
import com.order.repository.OrderRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPersistenceService {
    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    // failure cause: when Temporary failures eg. DB connection timeout    Network glitch Deadlock
    // sol:  what it does Retries the method automatically.
    @Retry(name = "orderDBRetry",fallbackMethod = "saveToQueue")
    // failure : Service keeps failing e.g DB down for 2 minutes // 1000 requests → all hit DB → DB crashes
    // sol: With circuit breaker: Failures exceed threshold , → circuit opens → requests fail fast
    @CircuitBreaker(name = "orderDBCircuit",fallbackMethod = "saveToQueue")
    // failure: High traffic like e.g. 1000 requests/sec
    // sol: Bulkhead limits concurrent calls. config : max-concurrent-calls: 50 , only 50 db call other fail best sol.
    @Bulkhead(name = "orderBulkhead")
    public OrderDocument saveOrder(OrderDocument orderDocument) {
        log.info("Saving order to database");

        return orderRepository.save(orderDocument);
    }

    // Q: why this method in case db fallback :
    // Ans: I contain the damage using circuit breakers and queue writes to Kafka to prevent data loss
    public OrderDocument saveToQueue(OrderDocument orderDocument, Exception ex) {

        log.error("Database unavailable. Sending order to queue", ex);

        orderEventPublisher.publishOrderCreated(
                new OrderPublishRecord(
                        orderDocument.getOrderId(),
                        orderDocument.getUserId(),
                        100D
                )
        );

        return orderDocument;
    }
}
