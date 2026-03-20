package com.payment.webhook.controller;

import com.payment.webhook.event.PublishPaymentEvent;
import com.payment.webhook.utils.RequestValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/webhook")
@RestController
@RequiredArgsConstructor
public class PaymentWebhookController {
    private final PublishPaymentEvent publishPaymentEvent;
    private final RequestValidationUtil validationUtil;
    @PostMapping("/received-transaction")
    public ResponseEntity<String> paymentWebhook(@RequestBody String webHookRequest,
                       @RequestHeader(value = "X-Signature") String signature ) throws Exception {

         if(!validationUtil.isValidSignature(signature)){
             return ResponseEntity.ok("BAD CREDENTIALS");
         }
        publishPaymentEvent.processPayment(webHookRequest);
        return ResponseEntity.ok("accepted");
    }
}
