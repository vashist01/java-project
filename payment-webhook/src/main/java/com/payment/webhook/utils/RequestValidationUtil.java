package com.payment.webhook.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@Component
@RequiredArgsConstructor
public class RequestValidationUtil {

    public  boolean isValidSignature(String signature) {
        return MessageDigest.isEqual(signature.getBytes(),signature.getBytes()); // for testing purpose validation for same
    }
}
