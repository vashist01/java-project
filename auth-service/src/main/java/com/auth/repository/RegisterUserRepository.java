package com.auth.repository;

import com.auth.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterUserRepository extends MongoRepository<User, String> {
    Optional<User> findByMobileNumber(String mobileNumber);
}
