package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.CartRequest;
import com.example.ShopAppEcomere.dto.response.cart.CartBasicResponse;
import com.example.ShopAppEcomere.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "product",ignore = true)
    Cart toCart(CartRequest request);
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "productId", source = "product.id")
    CartBasicResponse toCartResponse(Cart cart);
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "product",ignore = true)
    void updateCart(@MappingTarget Cart cart, CartRequest request);
}