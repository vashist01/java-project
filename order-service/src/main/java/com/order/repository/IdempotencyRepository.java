package com.order.repository;

import com.order.entity.IdempotencyRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IdempotencyRepository extends MongoRepository<IdempotencyRecord,String> {
    Optional<IdempotencyRecord> findByIdempotencyKey(String idempotenceKey);
}
