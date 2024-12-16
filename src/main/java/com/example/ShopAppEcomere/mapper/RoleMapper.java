package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.RoleRequest;
import com.example.ShopAppEcomere.dto.response.RoleResponse;
import com.example.ShopAppEcomere.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true) //bỏ qua trường permissions
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
