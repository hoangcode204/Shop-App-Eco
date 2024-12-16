package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.ShipmentRequest;
import com.example.ShopAppEcomere.dto.response.ShipmentResponse;
import com.example.ShopAppEcomere.entity.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user",ignore = true)
    Shipment toShipment(ShipmentRequest request);
    @Mapping(target = "userId",source = "user.id")
    ShipmentResponse toShipmentResponse(Shipment shipment);

    // Cập nhật thông tin shipment từ request (dành cho update)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user",ignore = true)
    void updateShipment(@MappingTarget Shipment shipment, ShipmentRequest request);
}
