package com.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Idempotency {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String idempotencyKey;
    private String requestHash;
    private String responseBody;
    private LocalDateTime createAt;
}
