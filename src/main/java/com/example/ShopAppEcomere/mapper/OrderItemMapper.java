package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.OrderItemRequest;
import com.example.ShopAppEcomere.dto.response.orderItem.OrderItemBasicResponse;
import com.example.ShopAppEcomere.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true) // ignore id when creating a new order item
    @Mapping(target = "order",ignore = true)
    @Mapping(target = "product",ignore = true)
    OrderItem toOrderItem(OrderItemRequest request);

    // Cập nhật thông tin orderItem từ request (dành cho update)
    @Mapping(target = "order",ignore = true)
    @Mapping(target = "product",ignore = true)
    void updateOrderItem(@MappingTarget OrderItem orderItem, OrderItemRequest request);
}
