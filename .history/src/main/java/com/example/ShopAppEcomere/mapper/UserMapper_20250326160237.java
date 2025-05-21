package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.response.UserResponse;
import com.example.ShopAppEcomere.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
