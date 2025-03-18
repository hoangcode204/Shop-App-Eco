package com.example.ShopAppEcomere.dto.request;

import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.enums.OrderStatusEnum;
import com.example.ShopAppEcomere.enums.ShipmentStatusEnum;
import com.example.ShopAppEcomere.validator.OrderStatusSubset;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class ShipmentRequest {

    @NotNull(message = "Shipment address is required")
    private String address;

    @NotNull(message = "Zip code is required")
    private String zipCode;
    @NotNull(message = "Order status is required")
    @OrderStatusSubset(anyOf = {OrderStatusEnum.PENDING, OrderStatusEnum.CONFIRMED, OrderStatusEnum.SHIPPED},
            message = "Invalid order status")
    private ShipmentStatusEnum status;

    private Date shipmentDate;
    private Long  userId;
}
