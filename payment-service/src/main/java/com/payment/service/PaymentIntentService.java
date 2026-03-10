package com.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.dto.request.InitiatePaymentRequestDTO;
import com.payment.dto.response.PaymentResponse;
import com.payment.entity.Idempotency;
import com.payment.entity.PaymentIntent;
import com.payment.repository.IdempotenceRepository;
import com.payment.repository.PaymentIntentRepository;
import com.payment.util.IdempotencyConstant;
import com.payment.util.SnowflakeIdGenerator;
import jakarta.transaction.Transactional;
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
@RequiredArgsConstructor
@Slf4j

public class PaymentIntentService {

    private final PaymentIntentRepository paymentIntentRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final IdempotenceRepository idempotenceRepository;
    private final ObjectMapper objectMapper;
    @Transactional
    public PaymentResponse initiatePaymentUrl(InitiatePaymentRequestDTO request, String idempotencyKey) {

        String cacheKey = "idempotency-key:"+idempotencyKey;

        log.info("idempotence key : {}",cacheKey);

        boolean lockAcquired = acquireLock(idempotencyKey);
        if (!lockAcquired) {
            throw new ValidateException("Request already processing");
        }
        try{
            String cacheResponse = redisTemplate.opsForValue().get(cacheKey);

            if(cacheResponse!=null){

                log.warn("Duplicate Request with same param ");
                throw new ValidateException("Duplicate Request Found with same idempotency key :");
            }

            Optional<Idempotency>  idempontencyOptional = idempotenceRepository.findByIdempotencyKey(idempotencyKey);
            if(idempontencyOptional.isPresent()){
                log.warn("Duplicate Request with same param ");
                throw new ValidateException("Duplicate Request Found with same idempotency key :");
            }

            PaymentIntent paymentIntent = PaymentIntent.builder().paymentId( SnowflakeIdGenerator.getUniqeTransactionId())
                    .createdAt(LocalDateTime.now()).orderId(request.getOrderId()).paymentUrl(MockService.getPaymentUrl(request)).
                    expiresAt(LocalDateTime.now().plusMinutes(10)).amount(request.getAmount()).build();

            paymentIntentRepository.save(paymentIntent);

            addIdempotency(idempotencyKey,request);

            updateRedisCacheByIdempotencyKey(cacheKey,request);

            return PaymentResponse.builder().paymentUrl(paymentIntent.getPaymentUrl())
                    .transactionId(paymentIntent.getPaymentId()).amount(paymentIntent.getAmount())
                    .orderId(paymentIntent.getOrderId()).build();
        }catch (Exception exception){
            throw new ValidateException("Failed to initiate payment due some technical issue.");
        }finally{
            releaseLock(idempotencyKey);
        }

    }

    private void releaseLock(String idempotencyKey) {
        String lockKey = IdempotencyConstant.LOCK_KEY+ idempotencyKey;
        redisTemplate.delete(lockKey);
    }

    private void updateRedisCacheByIdempotencyKey(String idempotanceKey, InitiatePaymentRequestDTO request) {
        redisTemplate.opsForValue().set(idempotanceKey,getJsonString(request),Duration.ofHours(24));
    }


    private void addIdempotency(String idempotencyKey, InitiatePaymentRequestDTO request) {
        Idempotency idempotency = new Idempotency();
        idempotency.setIdempotencyKey(idempotencyKey);
        idempotency.setRequestHash(hashRequest(request));
        idempotency.setResponseBody(getJsonString(request));
        idempotenceRepository.save(idempotency);
    }

    private String hashRequest(InitiatePaymentRequestDTO request) {
        String jsonString = getJsonString(request);
        return DigestUtils.sha256Hex(jsonString);
    }

    private String getJsonString(InitiatePaymentRequestDTO request) {
        try{
            return objectMapper.writeValueAsString(request);
        }catch (JsonProcessingException jsonParseException){
            log.error("Error serializing OrderRequestDTO", jsonParseException);

            throw new IllegalStateException(
                    "Unable to serialize order request", jsonParseException);
        }
    }

    private boolean acquireLock(String idempotencyKey) {
        String lockKey =IdempotencyConstant.LOCK_KEY + idempotencyKey;
        Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "LOCK", Duration.ofSeconds(10));
        return Boolean.TRUE.equals(locked);

    }
}
