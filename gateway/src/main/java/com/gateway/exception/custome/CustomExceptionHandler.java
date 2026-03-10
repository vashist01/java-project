package com.gateway.exception.custome;


import com.gateway.dto.response.ResponseBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
@Slf4j
public class CustomExceptionHandler {
    int statusCode = HttpStatus.OK.value();
    ObjectMapper objectMapper = new ObjectMapper();
    public ResponseEntity<ResponseBO> successResponse(Object object){

        log.info("Response Data : {}",objectMapper.writeValueAsString(object));
        return  ResponseEntity.ok(new ResponseBO(object,"Success", statusCode,
                LocalDateTime.now().toString()));
    }

    public ResponseEntity<ResponseBO> failureResponse(ResponseBO responseBO){
        return  ResponseEntity.ok(responseBO);
    }
}
