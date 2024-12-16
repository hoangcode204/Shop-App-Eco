package com.example.ShopAppEcomere.dto.response.orderItem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
public class OrderItemBasicResponse {

    private Long id;
    private int numberOfProducts;
    private Float totalMoney;
}
