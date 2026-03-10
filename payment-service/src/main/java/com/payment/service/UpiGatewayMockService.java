package com.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpiGatewayMockService {

    public boolean pay(BigDecimal amount){
        // 75% success rate
        return Math.random() < 0.75;
    }
    public String getPaymentId(){
        return "UPI-"+ UUID.randomUUID();
    }

}
