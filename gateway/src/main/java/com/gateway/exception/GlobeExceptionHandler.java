package com.gateway.exception;


import com.gateway.dto.response.ResponseBO;
import com.gateway.exception.custome.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;


@Slf4j
public class GlobeExceptionHandler extends CustomExceptionHandler {
    @ExceptionHandler(UnAuthorizeException.class)
    public ResponseEntity<ResponseBO> unAuthorizeException(UnAuthorizeException unAuthorizeException){
        log.error("Exception : {}",unAuthorizeException);
        ResponseBO responseBO = new ResponseBO(null,unAuthorizeException.getMessage(),unAuthorizeException.getStatusCode(),
                LocalDateTime.now().toString());
        return failureResponse(responseBO);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ResponseBO> userAlreadyExist(UserAlreadyExistException userAlreadyExistException){
        log.error("Exception : {}",userAlreadyExistException);
        ResponseBO responseBO = new ResponseBO(null,userAlreadyExistException.getMessage(),userAlreadyExistException.getStatusCode(),
                LocalDateTime.now().toString());
        return failureResponse(responseBO);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBO> MethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        log.error("Exception : {}",methodArgumentNotValidException);
        ResponseBO responseBO = new ResponseBO(null,methodArgumentNotValidException.getBindingResult().
                getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now().toString());
        return failureResponse(responseBO);
    }

    @ExceptionHandler(MissingRequestHeader.class)
    public ResponseEntity<ResponseBO> MethodArgumentNotValidException(MissingRequestHeader missingRequestHeader
                                                                     ){
        log.error("Exception : {}",missingRequestHeader);
        ResponseBO responseBO = new ResponseBO(null,missingRequestHeader.getMessage(), missingRequestHeader.getStatusCode(),
                LocalDateTime.now().toString());
        return failureResponse(responseBO);
    }

    @ExceptionHandler(IdempotencyException.class)
    public ResponseEntity<ResponseBO> handleIdempotency(IdempotencyException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseBO(null, ex.getMessage(),409,LocalDateTime.now().toString()));
    }
}
