package com.auth.exception.custome;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException{
    private String message;
    private int statusCode;
}
