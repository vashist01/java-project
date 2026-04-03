package com.payment.repository;

import com.payment.entity.SagaTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SagaRepository extends JpaRepository<SagaTransaction, Long> {
    Optional<SagaTransaction> findByTransactionId(String transactionId);
}