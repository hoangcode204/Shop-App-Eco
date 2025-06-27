package com.example.ShopAppEcomere.dto.response.orderItem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OrderItemResponse {

    private Integer id;
    private Integer quantity;
    private Integer productId;
    private Float amount;
}
