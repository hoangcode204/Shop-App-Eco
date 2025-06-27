package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.RoleRequest;
import com.example.ShopAppEcomere.dto.response.RoleResponse;
import com.example.ShopAppEcomere.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    // Chuyển đổi từ RoleRequest sang Role
    @Mapping(target = "permissions", ignore = true) // Bỏ qua trường permissions nếu không cần thiết
    Role toRole(RoleRequest request);

    // Chuyển đổi từ Role sang RoleResponse
    @Named("mapRoles")
    @Mapping(target = "permissions", ignore = true) // Bỏ qua trường permissions nếu không cần thiết
    RoleResponse toRoleResponse(Role role);
}
