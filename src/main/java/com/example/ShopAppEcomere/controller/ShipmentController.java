package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.ShipmentRequest;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.ShipmentResponse;
import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.entity.Shipment;
import com.example.ShopAppEcomere.service.ShipmentService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping("/shipments")
    public ApiResponse<ShipmentResponse> createShipment(@Valid @RequestBody ShipmentRequest request) {
        return ApiResponse.<ShipmentResponse>builder()
                .result(shipmentService.createShipment(request))
                .build();
    }

    @GetMapping("/shipments/{shipmentId}")
    public ApiResponse<ShipmentResponse> getShipmentById(@PathVariable("shipmentId") Long shipmentId) {
        return ApiResponse.<ShipmentResponse>builder()
                .result(shipmentService.getShipmentById(shipmentId))
                .build();
    }

    @GetMapping("/shipments")
    public ApiResponse<ResultPaginationDTO> getAllShipments(
            @Filter Specification<Shipment> spec,
            Pageable pageable) {
        return ApiResponse.<ResultPaginationDTO>builder()
                .result(shipmentService.fetchAllShipments(spec, pageable))
                .build();
    }

    @PutMapping("/shipments/{shipmentId}")
    public ApiResponse<ShipmentResponse> updateShipment(
            @PathVariable Long shipmentId,
            @RequestBody ShipmentRequest request) {
        return ApiResponse.<ShipmentResponse>builder()
                .result(shipmentService.updateShipment(request, shipmentId))
                .build();
    }

    @DeleteMapping("/shipments/{shipmentId}")
    public ApiResponse<Void> deleteShipment(@PathVariable("shipmentId") Long shipmentId) throws AccessDeniedException {
        shipmentService.deleteShipment(shipmentId);
        return ApiResponse.<Void>builder()
                .message("Delete shipment successful")
                .build();
    }
}
