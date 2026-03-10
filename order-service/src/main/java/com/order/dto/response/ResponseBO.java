package com.order.dto.response;

import java.time.LocalDateTime;

public record  ResponseBO(Object object, String message,  boolean status, int statusCode, LocalDateTime localDateTime) {
}
