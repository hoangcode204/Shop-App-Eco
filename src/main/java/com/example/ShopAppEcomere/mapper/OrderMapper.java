package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.response.discount.DiscountResponse;
import com.example.ShopAppEcomere.dto.response.order.OrderResponse;
import com.example.ShopAppEcomere.dto.response.order.OrderStatusResponse;
import com.example.ShopAppEcomere.dto.response.orderItem.OrderItemResponse;
import com.example.ShopAppEcomere.entity.Discount;
import com.example.ShopAppEcomere.entity.Order;
import com.example.ShopAppEcomere.entity.OrderItem;
import com.example.ShopAppEcomere.entity.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "status", target = "status", qualifiedByName = "mapOrderStatus")
    @Mapping(source = "orderItems", target = "orderItems", qualifiedByName = "mapOrderItems")
    @Mapping(source = "discount", target = "discount", qualifiedByName = "mapDiscount")
    @Mapping(source = "vnpTxnRef", target = "vnpTxnRef")
    @Mapping(source = "isPaid", target = "isPaid")
    OrderResponse toOrderResponse(Order order);

    @Named("mapOrderStatus")
    default OrderStatusResponse mapOrderStatus(OrderStatus status) {
        if (status == null) return null;
        OrderStatusResponse response = new OrderStatusResponse();
        response.setStatus(status.getStatus());
        return response;
    }

    @Named("mapOrderItems")
    default List<OrderItemResponse> mapOrderItems(List<OrderItem> items) {
        if (items == null) return null;
        return items.stream().map(item ->
                OrderItemResponse.builder()
                        .id(item.getId())
                        .quantity(item.getQuantity())
                        .productId(item.getProduct().getId())
                        .amount(item.getAmount())
                        .build()
        ).collect(Collectors.toList());
    }

    @Named("mapDiscount")
    default DiscountResponse mapDiscount(Discount discount) {
        if (discount == null) return null;
        return DiscountResponse.builder()
                .id(discount.getId())
                .name(discount.getName())
                .description(discount.getDescription())
                .discount_percent(discount.getDiscount_percent())
                .is_active(discount.getIs_active())
                .build();
    }
}