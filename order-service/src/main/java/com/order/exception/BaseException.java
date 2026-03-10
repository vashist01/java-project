package com.order.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    private String message;
    private int statusCode;
}
