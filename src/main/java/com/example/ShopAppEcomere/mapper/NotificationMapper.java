package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.response.NotificationResponse;
import com.example.ShopAppEcomere.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "orderId", source = "order.id")
    NotificationResponse toNotificationResponse(Notification notification);
}