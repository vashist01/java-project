package com.order.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.order.dto.request.ItemRequestDTO;
import com.order.dto.request.OrderRequestDTO;
import com.order.entity.OrderDocument;
import com.order.entity.ProductDocument;
import com.order.enums.OrderStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
@Component
@RequiredArgsConstructor
public class OrderValidationHelper {

    private final Gson gson;
    public OrderDocument convertRequestDtoToMongoDocument(OrderRequestDTO orderRequestDTO) {
        if( Objects.isNull(orderRequestDTO)){
            throw new RuntimeException("Please Select at least one order");
        }
        List<ProductDocument> productDocumentList;
        if(CollectionUtils.isEmpty(orderRequestDTO.getItems())){
            throw new RuntimeException("Please Select at least one item");
        }
        productDocumentList = convertItemRequestDtoToProductDocument(orderRequestDTO.getItems()) ;
     return OrderDocument.builder().orderStatus(OrderStatusEnum.INPROGRESS.name()).addressId(orderRequestDTO.getAddressId()
     ).paymentMethod(orderRequestDTO.getPaymentMethod()).productList(productDocumentList).build();
    }

    private List<ProductDocument> convertItemRequestDtoToProductDocument(List<ItemRequestDTO> items) {
        String json = gson.toJson(items);
        Type type = new TypeToken<List<ProductDocument>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
