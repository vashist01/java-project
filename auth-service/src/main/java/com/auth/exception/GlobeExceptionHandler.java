package com.auth.exception;

import com.auth.dto.response.ResponseBO;
import com.auth.exception.custome.CustomExceptionHandler;
import com.auth.exception.custome.MissingRequestHeader;
import com.auth.exception.custome.UnAuthorizeException;
import com.auth.exception.custome.UserAlreadyExistException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobeExceptionHandler extends CustomExceptionHandler {
    @ExceptionHandler(UnAuthorizeException.class)
    public ResponseEntity<ResponseBO> unAuthorizeException(UnAuthorizeException unAuthorizeException,
                                                           HttpServletRequest httpServletRequest){
        log.error("Exception : {}",unAuthorizeException);
        ResponseBO responseBO = new ResponseBO(null,unAuthorizeException.getMessage(),unAuthorizeException.getStatusCode(),
                LocalDateTime.now().toString(),httpServletRequest.getRequestedSessionId());
        return failureResponse(responseBO);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ResponseBO> userAlreadyExist(UserAlreadyExistException userAlreadyExistException,
                                                       HttpServletRequest httpServletRequest){
        log.error("Exception : {}",userAlreadyExistException);
        ResponseBO responseBO = new ResponseBO(null,userAlreadyExistException.getMessage(),userAlreadyExistException.getStatusCode(),
                LocalDateTime.now().toString(),httpServletRequest.getRequestedSessionId());
        return failureResponse(responseBO);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBO> MethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException,
                                                       HttpServletRequest httpServletRequest){
        log.error("Exception : {}",methodArgumentNotValidException);
        ResponseBO responseBO = new ResponseBO(null,methodArgumentNotValidException.getBindingResult().
                getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now().toString(),httpServletRequest.getRequestedSessionId());
        return failureResponse(responseBO);
    }

    @ExceptionHandler(MissingRequestHeader.class)
    public ResponseEntity<ResponseBO> MethodArgumentNotValidException(MissingRequestHeader missingRequestHeader,
                                                                      HttpServletRequest httpServletRequest){
        log.error("Exception : {}",missingRequestHeader);
        ResponseBO responseBO = new ResponseBO(null,missingRequestHeader.getMessage(), missingRequestHeader.getStatusCode(),
                LocalDateTime.now().toString(),httpServletRequest.getRequestedSessionId());
        return failureResponse(responseBO);
    }
}
