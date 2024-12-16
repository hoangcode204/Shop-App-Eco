package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.OrderItemRequest;

import com.example.ShopAppEcomere.dto.response.ApiResponse;

import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.dto.response.orderItem.OrderItemDetailResponse;
import com.example.ShopAppEcomere.entity.OrderItem;
import com.example.ShopAppEcomere.service.OrderItemService;
import com.example.ShopAppEcomere.validator.ApiMessage;
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
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping("/orderitems")
    @ApiMessage("Create a new order")
    public ApiResponse<OrderItemDetailResponse> createOrder(@Valid @RequestBody OrderItemRequest request) {
        return ApiResponse.<OrderItemDetailResponse>builder()
                .result(orderItemService.createOrderItem(request))
                .build();
    }

    @GetMapping("/orderitems/{orderItemId}")
    @ApiMessage("Fetch order by id")
    public ApiResponse<OrderItemDetailResponse> getOrderById(@PathVariable("orderItemId") Long orderId) {
        return ApiResponse.<OrderItemDetailResponse>builder()
                .result(orderItemService.getOrderItemById(orderId))
                .build();
    }

    @GetMapping("/orderitems")
    @ApiMessage("Fetch all order items")
    public ApiResponse<ResultPaginationDTO> getAllOrders(
            @Filter Specification<OrderItem> spec,
            Pageable pageable) {
        return ApiResponse.<ResultPaginationDTO>builder()
                .result(orderItemService.fetchAllOrderItems(spec,pageable))
                .build();
    }

    @PutMapping("/orderitems/{orderItemId}")
    @ApiMessage("Update orderitem")
    public ApiResponse<OrderItemDetailResponse> updateOrder(
            @PathVariable Long orderItemId,
            @RequestBody OrderItemRequest request) {
        return ApiResponse.<OrderItemDetailResponse>builder()
                .result(orderItemService.updateOrderItem(request,orderItemId))
                .build();
    }

    @DeleteMapping("/orderitems/{orderItemId}")
    @ApiMessage("Delete orderitem")
    public ApiResponse<Void> deleteOrder(@PathVariable("orderItemId") Long orderItemId) throws AccessDeniedException {
        orderItemService.deleteOrderItem(orderItemId);
        return ApiResponse.<Void>builder()
                .message("Delete orderItem successful")
                .build();
    }
}
