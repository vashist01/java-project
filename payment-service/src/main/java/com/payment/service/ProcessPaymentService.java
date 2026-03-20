package com.payment.service;

import com.payment.FeignClientCall;
import com.payment.config.SecretKeyConfiguration;
import com.payment.util.AESUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessPaymentService {

    private final FeignClientCall feignClientCall;
    private final SecretKeyConfiguration propertiesConfiguration;

    @Async("taskExecutor")
    public void processPaymentToWebhook(String webhookRequest) throws Exception {
        String encryptedPayment = AESUtil.encrypt(webhookRequest);
        String generatedSignature = generateHmaSHA256("CREDENTIALS");
       feignClientCall.processPayment(encryptedPayment,generatedSignature);
    }


    private String generateHmaSHA256(String generatedSignature) throws Exception
    {
        String secretKey = propertiesConfiguration.secret();
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec =
                new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(generatedSignature.getBytes());
        return Base64.getEncoder().encodeToString(rawHmac);
    }
}
