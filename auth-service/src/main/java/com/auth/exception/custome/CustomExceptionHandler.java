package com.auth.exception.custome;

import com.auth.dto.response.ResponseBO;
import com.auth.enums.ResponseMessageEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
@Slf4j
public class CustomExceptionHandler {
    int statusCode = HttpStatus.OK.value();
    ObjectMapper objectMapper = new ObjectMapper();
    public ResponseEntity<ResponseBO> successResponse(Object object, HttpServletRequest httpServletRequest){

        log.info("Response Data : {}",objectMapper.writeValueAsString(object));
        return  ResponseEntity.ok(new ResponseBO(object, ResponseMessageEnum.SUCCESS.name(), statusCode,
                LocalDateTime.now().toString(),httpServletRequest.getRequestedSessionId()));
    }

    public ResponseEntity<ResponseBO> failureResponse(ResponseBO responseBO){
        return  ResponseEntity.ok(responseBO);
    }
}
