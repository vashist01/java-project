package com.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "product")
@Builder
public class ProductDocument {

    private String id;
    private String productId;
    private String quantity;
    private String price;
    private String productName;
    private String productImage;
}
