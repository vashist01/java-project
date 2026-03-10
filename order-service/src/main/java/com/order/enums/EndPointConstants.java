package com.order.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EndPointConstants {
    ;
    public static final String CLASS_LEVEL_API_MAPPING = "/api/order";
    public static final String CREATE_ORDER = "/create-order";
}
