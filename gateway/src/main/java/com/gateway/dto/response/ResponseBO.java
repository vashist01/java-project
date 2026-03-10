package com.gateway.dto.response;

public record ResponseBO(Object data,String message,int statusCode,String dateTime) {
}
