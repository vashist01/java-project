package com.payment.webhook.utils;

import lombok.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestValidationUtil {
    @Value("${webhook.secret}")
    private String secret;
    public   boolean isValidSignature(String signature) {
    }
}
