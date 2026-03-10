package com.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_intent")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntent {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentId;

    private String orderId;

    private Double amount;

    private String currency;

    private String vendorOrderId;

    private String status;

    private String paymentUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}