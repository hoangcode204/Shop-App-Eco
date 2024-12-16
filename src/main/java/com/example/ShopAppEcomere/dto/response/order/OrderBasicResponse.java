package com.example.ShopAppEcomere.dto.response.order;

import com.example.ShopAppEcomere.enums.OrderStatusEnum;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBasicResponse {
    private Long id;
    private Date orderDate;
    private Integer totalPrice;
    private Long userId;
    private Long shipmentId;
    private OrderStatusEnum status;
}