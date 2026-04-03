package com.payment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "saga_transaction")
@Data
public class SagaTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String transactionId;

    private String currentStep;

    @Enumerated(EnumType.STRING)
    private SagaStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}