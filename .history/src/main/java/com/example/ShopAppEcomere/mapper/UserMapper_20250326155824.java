package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.response.UserResponse;
import com.example.ShopAppEcomere.entity.User;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    User toUser(UserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    void updateUser(@MappingTarget User user, UserRequest request);

    UserResponse toUserResponse(User user);
}
