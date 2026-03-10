package com.payment.repository;

import com.payment.entity.Idempotency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotenceRepository extends JpaRepository<Idempotency,Long> {
    Optional<Idempotency> findByIdempotencyKey(String idempotenceKey);
}
