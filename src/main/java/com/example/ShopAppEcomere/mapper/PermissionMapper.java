package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.PermissionRequest;
import com.example.ShopAppEcomere.dto.response.PermissionResponse;
import com.example.ShopAppEcomere.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
