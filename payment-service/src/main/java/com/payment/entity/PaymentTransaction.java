package com.payment.entity;

import com.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="payment_transactions")
public class PaymentTransaction {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true,length = 30)
    private String transactionId;
    private String orderId;
    private String userId;
    private Double amount;
    private String currency;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String idempotencyKey;
    private LocalDateTime createAt;

}
