package com.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.dto.request.OrderRequestDTO;
import com.order.dto.response.OrderResponseDTO;
import com.order.entity.IdempotencyRecord;
import com.order.entity.OrderDocument;
import com.order.enums.OrderStatusEnum;
import com.order.event.OrderEventPublisher;
import com.order.helper.OrderValidationHelper;
import com.order.model.OrderPublishRecord;
import com.order.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderValidationHelper orderValidationHelper;
    private final OrderEventPublisher orderEventPublisher;
    private final RedisTemplate<String,String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final IdempotencyRepository idempotencyRepository;
    private final OrderPersistenceService orderPersitenceService;
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, String idempotenceKey) {

        String idempotencyKey = "idempontency:"+idempotenceKey;
        log.info("Creating order for  idempotencyKey={}", idempotenceKey);
        try{
            Thread.sleep(8000);   // simulate slow service

            log.warn("Simulated latency in order service");
            String cachedResponse = redisTemplate.opsForValue().get(idempotencyKey);

            if(cachedResponse!=null){

                log.info("Returning cached response for key {}", idempotencyKey);

                throw new ValidateException(
                        "Duplicate request not allowed with same idempotency key");
            }
            // Acquire Distributed Lock (Prevent Race Condition)
            boolean lockAcquired = acquiredLock(idempotenceKey);

            if(!lockAcquired){
                // another request is processing
                throw new ValidateException("Request already processing");
            }
            //DB Idempotency Check (Redis may lose data)
            Optional<IdempotencyRecord> optionalIdempontecnyKey =
                    idempotencyRepository.findByIdempotencyKey(idempotencyKey);

            if (optionalIdempontecnyKey.isPresent()) {
                log.warn("Duplicate order request detected for idempotencyKey={}", idempotencyKey);

                throw new ValidateException(
                        "Duplicate request not allowed with same idempotency key");
            }
            OrderDocument orderDocument =
                    orderValidationHelper.convertRequestDtoToMongoDocument(orderRequestDTO);

            //question: Why We Put  orderPersitenceService.saveOrder in Separate Service
            // ANS: spring aop is work only proxy method if we annotate a rejilance4 annotation in same class wit private then retry won't work
            OrderDocument savedOrder = orderPersitenceService.saveOrder(orderDocument);

            saveIdempotencyRecord(idempotencyKey,orderRequestDTO);

            updatecacheresponsebyidempontencykey(idempotencyKey,orderRequestDTO);

            if (savedOrder.getId() != null) {

                orderEventPublisher.publishOrderCreated(
                        new OrderPublishRecord(
                                savedOrder.getOrderId(),
                                savedOrder.getUserId(),
                                100D
                        )
                );
                return buildSuccessResponse(savedOrder);
            }
            return buildFailureResponse(orderDocument);
        }catch (Exception exception){

           } finally{
            // releasing lock
            redisTemplate.delete("idempotency:lock"+idempotenceKey);
        }

        return null;
    }

    private boolean acquiredLock(String idempotenceKey) {
        String lockKey = "idempotency:lock:" + idempotenceKey;

        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey,"LOCK",Duration.ofSeconds(10));

        return Boolean.TRUE.equals(success);
    }

    private void updatecacheresponsebyidempontencykey(String idempotenceKey, OrderRequestDTO orderRequestDTO) {
            redisTemplate.opsForValue().
                    set(idempotenceKey,getOrderDtoJsonString(orderRequestDTO)
                    , Duration.ofHours(24));

    }

    private String getOrderDtoJsonString(OrderRequestDTO orderRequestDTO) {

        try {
            return objectMapper.writeValueAsString(orderRequestDTO);

        } catch (JsonProcessingException e) {

            log.error("Error serializing OrderRequestDTO", e);

            throw new IllegalStateException(
                    "Unable to serialize order request", e);
        }
    }

    private void saveIdempotencyRecord(String idempotencyKey, OrderRequestDTO orderRequestDTO) {

            IdempotencyRecord idempotencyRecord = IdempotencyRecord.builder()

                    .idempotencyKey(idempotencyKey)

                    .requestHash(hashRequest(orderRequestDTO)).

                    responseBody(getOrderDtoJsonString(orderRequestDTO))

                    .createdAt(LocalDateTime.now()).build();

            idempotencyRepository.save(idempotencyRecord);
    }

    private String hashRequest(OrderRequestDTO orderRequestDTO) {
        String json = getOrderDtoJsonString(orderRequestDTO);
        return DigestUtils.sha256Hex(json);
    }

    private OrderResponseDTO buildSuccessResponse(OrderDocument order) {
        return new OrderResponseDTO(
                order.getOrderId(),
                order.getOrderStatus(),
                order.getOrderStatus(),
                order.getProductList().size()
        );
    }

    private OrderResponseDTO buildFailureResponse(OrderDocument order) {
        return new OrderResponseDTO(
                null,
                OrderStatusEnum.FAILED.name(),
                OrderStatusEnum.FAILED.name(),
                order.getProductList().size()
        );
    }
}