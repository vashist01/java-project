package com.order.exception;



import com.order.controller.GenericController;
import com.order.dto.response.ResponseBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;


@Slf4j
public class GlobeExceptionHandler extends GenericController {
    @ExceptionHandler(UnAuthorizeException.class)
    public ResponseEntity<ResponseBO> unAuthorizeException(UnAuthorizeException unAuthorizeException){
        log.error("Exception : {}",unAuthorizeException);
        ResponseBO responseBO = new ResponseBO(null,unAuthorizeException.getMessage(),false,unAuthorizeException.getStatusCode(),
                LocalDateTime.now());
        return ResponseEntity.ok(responseBO);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ResponseBO> userAlreadyExist(UserAlreadyExistException userAlreadyExistException){
        log.error("Exception : {}",userAlreadyExistException);
        ResponseBO responseBO = new ResponseBO(null,userAlreadyExistException.getMessage(),false,userAlreadyExistException.getStatusCode(),
                LocalDateTime.now());
        return ResponseEntity.ok(responseBO);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBO> MethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        log.error("Exception : {}",methodArgumentNotValidException);
        ResponseBO responseBO = new ResponseBO(null,methodArgumentNotValidException.getBindingResult().
                getFieldError().getDefaultMessage(),false, HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());
        return ResponseEntity.ok(responseBO);
    }

    @ExceptionHandler(MissingRequestHeader.class)
    public ResponseEntity<ResponseBO> MethodArgumentNotValidException(MissingRequestHeader missingRequestHeader
                                                                     ){
        log.error("Exception : {}",missingRequestHeader);
        ResponseBO responseBO = new ResponseBO(null,missingRequestHeader.getMessage(),false, missingRequestHeader.getStatusCode(),
                LocalDateTime.now());
        return ResponseEntity.ok(responseBO);
    }
}
