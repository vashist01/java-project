package com.order.controller;

import com.order.dto.response.ResponseBO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class GenericController {

    public ResponseEntity<ResponseBO> handleSuccess(Object object){
        return ResponseEntity.ok(new ResponseBO(object,"Success",true,200, LocalDateTime.now()));
    }

}
