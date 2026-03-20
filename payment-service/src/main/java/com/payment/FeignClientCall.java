package com.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "PAYMENT-WEBHOOK", url = "http://localhost:8765")
public interface FeignClientCall {

    @PostMapping("/webhook/received-transaction")
      ResponseEntity<String> processPayment(@RequestBody String webHookRequest,
                        @RequestHeader("X-Signature") String generatedSignature);
}
