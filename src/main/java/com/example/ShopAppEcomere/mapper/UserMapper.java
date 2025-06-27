package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.UserRequest;
import com.example.ShopAppEcomere.dto.response.UserResponse;
import com.example.ShopAppEcomere.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = RoleMapper.class)  // Sử dụng RoleMapper để map Role sang RoleResponse
public interface UserMapper {

    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)  // Bỏ qua mapping cho trường roles ở đây, sẽ xử lý sau

    User toUser(UserRequest request);

    @Mapping(target = "role", source = "roles", qualifiedByName = "mapRoles")  // Chuyển các roles thành role trong UserResponse
    UserResponse toUserResponse(User user);
}
