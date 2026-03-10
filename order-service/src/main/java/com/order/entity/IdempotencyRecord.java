package com.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "idempotency_records")
@Builder
@Getter
public class IdempotencyRecord {

    @Id
    private String idempotencyKey;

    @Field("request_hash")
    private String requestHash;

    @Field("response_body")
    private String responseBody;

    private LocalDateTime createdAt;
}