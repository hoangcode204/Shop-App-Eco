package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.OrderRequest;

import com.example.ShopAppEcomere.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Chuyển từ OrderRequest sang Order entity
    @Mapping(target = "user", ignore = true) // Bỏ qua để xử lý riêng
    @Mapping(target = "shipment", ignore = true) // Bỏ qua để xử lý riêng
    @Mapping(target = "orderItems", ignore = true) // Bỏ qua để xử lý riêng
    Order toOrder(OrderRequest request);


    // Cập nhật Order entity từ OrderRequest
    @Mapping(target = "user", ignore = true) // Bỏ qua vì user không thay đổi ở đây
    @Mapping(target = "shipment", ignore = true) // Bỏ qua vì shipment không thay đổi ở đây
    @Mapping(target = "orderItems", ignore = true) // Xử lý riêng nếu cần cập nhật orderItems
    void updateOrder(@MappingTarget Order order, OrderRequest request);
}
