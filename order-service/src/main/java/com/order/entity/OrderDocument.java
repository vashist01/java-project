package com.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order")
@Builder
@Getter
public class OrderDocument {
    @Id
    private String id;
    @Field("order_id")
    private String orderId;
    private String userId;
    private String addressId;
    private String paymentMethod;
    private String orderStatus;
    private String notes;
    private List<ProductDocument> productList;

}
