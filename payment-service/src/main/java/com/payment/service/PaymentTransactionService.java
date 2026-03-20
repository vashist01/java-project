package com.payment.service;

import com.payment.entity.FailedEvent;
import com.payment.entity.PaymentTransaction;
import com.payment.repository.FailedEventRepository;
import com.payment.repository.PaymentTransactionRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final FailedEventRepository failedEventRepository;
    @Transactional
    public void processPaymentTransaction(String webhookPayload) {
        try{
            PaymentTransaction webhookPaymentTransaction = new ObjectMapper().readValue(webhookPayload,PaymentTransaction.class);

            Optional<PaymentTransaction> existingPaymentTransaction = paymentTransactionRepository.
                    findByTransactionId(webhookPaymentTransaction.getTransactionId());
            if(existingPaymentTransaction.isEmpty()){

                log.info("Webhook successfully converted ands payment Transaction is saved");

                paymentTransactionRepository.save(webhookPaymentTransaction);

            }else {
                PaymentTransaction paymentTransaction = existingPaymentTransaction.get();
                paymentTransaction.setPaymentStatus(webhookPaymentTransaction.getPaymentStatus());
                paymentTransaction.setAmount(webhookPaymentTransaction.getAmount());
                paymentTransactionRepository.save(paymentTransaction);
            }

        }catch (OptimisticLockException optimisticLockException){
            throw new RuntimeException(
                    "Concurrent update detected for transaction",optimisticLockException);
        } catch (Exception exception){
            log.error("Error process webhook Transaction",exception);
        }
    }

    @Transactional
    public void processFailedTransaction(String event) {
        FailedEvent failed = new FailedEvent();
        failed.setPayload(event);
        failed.setTopic("payment-webhook-event");
        failed.setError("Processing failed");
        failed.setFailedAt(LocalDateTime.now());
        failedEventRepository.save(failed);
    }
}
