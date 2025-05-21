package com.example.ShopAppEcomere.dto.response.orderItem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OrderItemResponse {

    private Long id;
    private Integer quantity;
    private Long productId;
    private Long amount;
}
