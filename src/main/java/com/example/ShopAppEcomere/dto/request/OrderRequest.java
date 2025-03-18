package com.example.ShopAppEcomere.dto.request;

import com.example.ShopAppEcomere.enums.OrderStatusEnum;
import com.example.ShopAppEcomere.validator.OrderStatusSubset;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "orderDate không được để trống")
    @PastOrPresent(message = "orderDate phải là ngày trong quá khứ hoặc hiện tại")
    private Date orderDate;
    @Positive(message = "price phải là số dương")
    private Integer totalPrice;
    @Min(value = 1,message = "userId lon hon 1")
    private Long userId;
    @Min(value = 1,message = "ShipmentId lon hon 1")
    private Long shipmentId;

    @NotNull(message = "Order status is required")
    @OrderStatusSubset(anyOf = {OrderStatusEnum.PENDING, OrderStatusEnum.CONFIRMED, OrderStatusEnum.SHIPPED},
            message = "Invalid order status")
    private OrderStatusEnum status;
    private List<Long> orderItemIds;
}
