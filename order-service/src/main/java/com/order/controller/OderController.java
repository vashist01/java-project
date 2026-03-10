package com.order.controller;

import com.order.dto.request.OrderRequestDTO;
import com.order.dto.response.OrderResponseDTO;
import com.order.dto.response.ResponseBO;
import com.order.enums.EndPointConstants;
import com.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(EndPointConstants.CLASS_LEVEL_API_MAPPING)
public class OderController extends GenericController{

    private final OrderService orderService;
    @PostMapping(EndPointConstants.CREATE_ORDER)
    public ResponseEntity<ResponseBO> createOrder(
            @RequestHeader("idempontency-key") String idempontencyKey,
            @RequestBody @Valid OrderRequestDTO orderRequestDTO){
        OrderResponseDTO orderResponseDTO = orderService.createOrder(orderRequestDTO,idempontencyKey);
        return handleSuccess(orderResponseDTO);
    }
}
