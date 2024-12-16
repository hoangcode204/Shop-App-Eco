package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.OrderRequest;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.dto.response.order.OrderBasicResponse;
import com.example.ShopAppEcomere.dto.response.order.OrderDetailResponse;
import com.example.ShopAppEcomere.entity.Order;
import com.example.ShopAppEcomere.service.OrderService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    @ApiMessage("Create a new order")
    public ApiResponse<OrderBasicResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        return ApiResponse.<OrderBasicResponse>builder()
                .result(orderService.createOrder(request))
                .build();
    }

    @GetMapping("/{orderId}")
    @ApiMessage("Fetch order by id")
    public ApiResponse<OrderDetailResponse> getOrderById(@PathVariable("orderId") Long orderId) {
        return ApiResponse.<OrderDetailResponse>builder()
                .result(orderService.getOrderById(orderId))
                .build();
    }
    @GetMapping("")
    @ApiMessage("Fetch all orders")
    public ApiResponse<ResultPaginationDTO> getAllOrders(
            @Filter Specification<Order> spec,
            Pageable pageable) {
        return ApiResponse.<ResultPaginationDTO>builder()
                .result(orderService.fetchAllOrders(spec, pageable))
                .build();
    }
    @GetMapping("/users/{userId}")
    @ApiMessage("Fetch all orders by user")
    public ApiResponse<ResultPaginationDTO> getAllOrdersByUser(@PathVariable("userId") Long userId,
            Pageable pageable) {
        return ApiResponse.<ResultPaginationDTO>builder()
                .result(orderService.fetchAllOrdersByUser(userId,pageable))
                .build();
    }
    @PutMapping("/{orderId}")
    @ApiMessage("Update order")
    public ApiResponse<OrderBasicResponse> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderRequest request) {
        return ApiResponse.<OrderBasicResponse>builder()
                .result(orderService.updateOrder(request, orderId))
                .build();
    }

    @DeleteMapping("/{orderId}")
    @ApiMessage("Delete order")
    public ApiResponse<Void> deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrder(orderId);
        return ApiResponse.<Void>builder()
                .message("Delete order successful")
                .build();
    }
}
