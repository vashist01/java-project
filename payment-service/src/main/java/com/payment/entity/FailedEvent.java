package com.payment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "failed_event")
public class FailedEvent {
    @Id
    @GeneratedValue
    private Long id;

    private String topic;
    private String payload;
    private String error;
    private LocalDateTime failedAt;

}
