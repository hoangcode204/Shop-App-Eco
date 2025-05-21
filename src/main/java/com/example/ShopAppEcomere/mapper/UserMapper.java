package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.UserRequest;
import com.example.ShopAppEcomere.dto.response.UserResponse;
import com.example.ShopAppEcomere.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(UserRequest request);
    UserResponse toUserResponse(User user);
}
