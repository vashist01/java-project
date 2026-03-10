package com.gateway.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum ServiceNameEnum {

    USER_SERVICE("user-service"), PAYMENT_SERVICE("payment-service"),
    ORDER_SERVICE("order-service")
    ;


    private final String serviceName;
}
