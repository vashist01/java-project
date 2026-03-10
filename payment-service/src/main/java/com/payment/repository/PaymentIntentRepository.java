package com.payment.repository;

import com.payment.entity.PaymentIntent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentIntentRepository
        extends JpaRepository<PaymentIntent,Long> {
    }
