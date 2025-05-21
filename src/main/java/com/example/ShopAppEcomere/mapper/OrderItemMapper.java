package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.response.orderItem.OrderItemResponse;
import com.example.ShopAppEcomere.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "product.id", target = "productId")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

}
