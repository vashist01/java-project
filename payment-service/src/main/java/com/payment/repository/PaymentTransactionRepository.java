package com.payment.repository;

import com.payment.entity.PaymentTransaction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PaymentTransaction> findByTransactionId(String transactionId);
}
