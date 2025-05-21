package com.example.ShopAppEcomere.mapper;
import com.example.ShopAppEcomere.dto.response.discount.DiscountResponse;
import com.example.ShopAppEcomere.entity.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    // Chuyển đổi từ Discount sang DiscountResponse
    DiscountResponse toDiscountResponse(Discount discount);
}
