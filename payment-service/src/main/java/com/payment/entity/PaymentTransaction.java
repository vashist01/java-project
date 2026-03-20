package com.payment.entity;

import com.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="payment_transactions")
@DynamicUpdate
public class PaymentTransaction {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,length = 30)
    private String transactionId;
    private String orderId;
    private String userId;
    private Double amount;
    private String currency;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private LocalDateTime createAt;
    @Version
    private Integer version;
}
