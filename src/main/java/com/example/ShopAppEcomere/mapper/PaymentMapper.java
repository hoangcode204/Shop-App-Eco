package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.PaymentRequest;
import com.example.ShopAppEcomere.dto.response.PaymentResponse;
import com.example.ShopAppEcomere.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true) // Ignore ID while creating new payment
    @Mapping(target = "user",ignore = true)
    Payment toPayment(PaymentRequest request);
    @Mapping(target = "userId",source = "user.id")
    PaymentResponse toPaymentResponse(Payment payment);

    // Cập nhật thông tin payment từ request (dành cho update)
    @Mapping(target = "user",ignore = true)
    void updatePayment(@MappingTarget  Payment payment, PaymentRequest request);
}
