package com.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Setter
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderRequestDTO {
    private List<ItemRequestDTO> items;
    @NotBlank(message = "address_id is required")
    private String addressId;
    @NotBlank(message = "payment_method is required")
    private String paymentMethod;
    private String notes;
    @NotNull(message = "idempotence_key is required always unique for every request")
    private String idempotenceKey;
}
