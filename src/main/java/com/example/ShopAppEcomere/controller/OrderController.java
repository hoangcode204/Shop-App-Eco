package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.OrderItem.OrderItemDTO;
import com.example.ShopAppEcomere.dto.request.order.*;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.order.OrderResponse;
import com.example.ShopAppEcomere.dto.response.orderItem.OrderItemResponse;
import com.example.ShopAppEcomere.entity.Order;
import com.example.ShopAppEcomere.entity.OrderAccount;
import com.example.ShopAppEcomere.entity.OrderItem;
import com.example.ShopAppEcomere.entity.OrderStatus;
import com.example.ShopAppEcomere.enums.StatusOrder;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.OrderMapper;
import com.example.ShopAppEcomere.repository.OrderRepository;
import com.example.ShopAppEcomere.service.OrderAccService;
import com.example.ShopAppEcomere.service.OrderItemService;
import com.example.ShopAppEcomere.service.OrderService;
import com.example.ShopAppEcomere.service.OrderStatusService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final OrderStatusService orderStatusService;
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final OrderAccService orderAccService;

    @GetMapping("")
    @ApiMessage("Fetch all orders")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<List<OrderResponse>> getAll() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getAllOrder())
                .build();
    }
    @GetMapping("/status/{id}")
    @ApiMessage("Fetch all orders by status id")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<List<OrderResponse>> getAllOrderByStatus(@PathVariable("id") Integer id) {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getAllOrderByStatus(id))
                .build();
    }
    @GetMapping("/status/{userId}/{statusId}")
    @ApiMessage("Fetch all orders by userid and status id")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ApiResponse<List<OrderResponse>> getAllOrderByStatus(@PathVariable("userId") Integer user,
                                                                @PathVariable("statusId") Integer id) {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getAllOrderByStatus(id))
                .build();
    }
    @GetMapping("/user/{id}")
    @ApiMessage("Get all orders by user ")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ApiResponse<List<OrderResponse>> getOrdersByAccount(@PathVariable("id") Integer id) {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrdersByAccount(id))
                .build();
    }

    @GetMapping("/statistical/totalprice")
    @ApiMessage("Get all product by status ")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<List<OrderStatusStatisticalDTO>> getOrderStatus() {
        return ApiResponse.<List<OrderStatusStatisticalDTO>>builder()
                .result(orderService.getOrderStatusStatistical())
                .build();
    }

    @GetMapping("/statistical/topproduct")
    @ApiMessage("Get top product ")
    public ApiResponse<List<OrderTopProductStatisticalDTO>> getProductTop() {
        return ApiResponse.<List<OrderTopProductStatisticalDTO>>builder()
                .result(orderService.getTopProduct())
                .build();
    }

    @GetMapping("/statistical/topaccounts")
    @ApiMessage("Get top account ")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<List<OrderTopAccountStatisticalDTO>> getTopAccount() {
        return ApiResponse.<List<OrderTopAccountStatisticalDTO>>builder()
                .result(orderService.getTopAccount())
                .build();
    }

    @PostMapping("/statistical/year")
    @ApiMessage("Get order by year ")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<List<OrderYearStatisticalDTO>> getOrderByYear(@RequestParam("year") String year) {
        return ApiResponse.<List<OrderYearStatisticalDTO>>builder()
                .result(orderService.getAllOrderByYear(year))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable Integer id) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderMapper.toOrderResponse(orderService.getOrderById(id)))
                .build();
    }

    @PostMapping()
    @ApiMessage("Create new order")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ApiResponse<OrderResponse> paymentProduct(@RequestBody OrderDTO orderRequest) throws Exception {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(orderRequest))
                .build();
    }

    @PutMapping("/shipping")
    @ApiMessage("Xác nhận đơn hàng")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ApiResponse<OrderResponse> shippingOrder(@RequestParam("order_id") Integer id) {
        Order order = orderService.getOrderById(id);
        OrderStatus status = orderStatusService.findOrderbyId(StatusOrder.CHO_XAC_NHAN);

        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_EXISTED);
        }

        if (order.getStatus().equals(status)) {
            OrderStatus orderStatus = orderStatusService.findOrderbyId(StatusOrder.DANG_GIAO);
            order.setStatus(orderStatus);
            orderRepository.save(order);
            return ApiResponse.<OrderResponse>builder()
                    .result(orderMapper.toOrderResponse(order))
                    .build();
        } else {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }
    }

    @PutMapping("/complete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @ApiMessage("Hoàn thành đơn hàng")
    public ApiResponse<OrderResponse> completeOrder(@RequestParam("order_id") Integer id) {
        Order order = orderService.getOrderById(id);
        OrderStatus status = orderStatusService.findOrderbyId(StatusOrder.DANG_GIAO);

        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_EXISTED);
        }

        if (order.getStatus().getId().equals(status.getId())) {
            OrderStatus orderStatus = orderStatusService.findOrderbyId(StatusOrder.DA_GIAO);
            order.setStatus(orderStatus);
            orderRepository.save(order);
            return ApiResponse.<OrderResponse>builder()
                    .result(orderMapper.toOrderResponse(order))
                    .build();
        } else {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }
    }
    @PutMapping("/cancel")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @ApiMessage("Cancel order by ID")
    public ApiResponse<OrderResponse> cancelStatusOrder(@RequestParam("order_id") Integer id) {
        Order orderCheck = orderService.getOrderById(id);
        if (orderCheck == null) {
            return ApiResponse.<OrderResponse>builder()
                    .message("Order not found")
                    .build();
        }
        OrderResponse order = orderService.setStatusCancel(id);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    @PostMapping("/postdetails")
    @ApiMessage("Create order details")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ApiResponse<OrderItemResponse> postDetails(@RequestBody OrderItemDTO orderItemRequest) {
        return ApiResponse.<OrderItemResponse>builder()
                .result(orderItemService.createOrderDetails(orderItemRequest))
                .build();
    }

    @GetMapping("/details")
    @ApiMessage("Get all order details")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ApiResponse<List<OrderItemResponse>> getAllDetails() {
        return ApiResponse.<List<OrderItemResponse>>builder()
                .result(orderItemService.getAll())
                .build();
    }
    @GetMapping("/details/{idacc}/{status}")
    @ApiMessage("Get order by account and status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ApiResponse<List<OrderAccount>> getOrderByAccAndStatus(@PathVariable("idacc") Integer idacc,
                                                                  @PathVariable("status") Integer status) {
        return ApiResponse.<List<OrderAccount>>builder()
                .result(orderAccService.getOrderByAccAndStatus(idacc, status))
                .build();
    }

    @GetMapping("/details/{idacc}")
    @ApiMessage("Get order by account")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public List<OrderAccount> getOrderByAcc(@PathVariable("idacc") Integer idacc) {
        return orderAccService.getOrderByAcc(idacc);
    }


}