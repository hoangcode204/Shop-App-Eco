package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.response.order.OrderResponse;
import com.example.ShopAppEcomere.dto.response.order.OrderStatusResponse;
import com.example.ShopAppEcomere.entity.Order;
import com.example.ShopAppEcomere.entity.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "status", target = "status", qualifiedByName = "mapOrderStatus")
    OrderResponse toOrderResponse(Order order);

    @Named("mapOrderStatus")
    default OrderStatusResponse mapOrderStatus(OrderStatus status) {
        if (status == null) {
            return null;
        }
        OrderStatusResponse response = new OrderStatusResponse();
        response.setStatus(status.getStatus());
        return response;
    }

}
