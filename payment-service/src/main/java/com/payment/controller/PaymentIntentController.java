package com.payment.controller;

import com.payment.dto.request.InitiatePaymentRequestDTO;
import com.payment.dto.response.PaymentResponse;
import com.payment.service.PaymentIntentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentIntentController {
    private final PaymentIntentService paymentService;
    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> createPayment(@RequestHeader("idempotency-key") String idempotencyKey,
                                                         @RequestBody InitiatePaymentRequestDTO request) {
        PaymentResponse response = paymentService.initiatePaymentUrl(request,idempotencyKey);
        return ResponseEntity.ok(response);
    }
}
