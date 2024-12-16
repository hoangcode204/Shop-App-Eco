package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.OrderRequest;
import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.dto.response.order.OrderBasicResponse;
import com.example.ShopAppEcomere.dto.response.order.OrderDetailResponse;
import com.example.ShopAppEcomere.dto.response.product.ProductBasicResponse;
import com.example.ShopAppEcomere.entity.*;
import com.example.ShopAppEcomere.enums.OrderStatusEnum;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.OrderMapper;
import com.example.ShopAppEcomere.repository.OrderItemRepository;
import com.example.ShopAppEcomere.repository.OrderRepository;
import com.example.ShopAppEcomere.repository.ShipmentRepository;
import com.example.ShopAppEcomere.repository.UserRepository;
import com.example.ShopAppEcomere.specifications.OrderSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final ShipmentRepository shipmentRepository;
    private final OrderItemRepository orderItemRepository;
    private List<OrderItem> fetchOrderItemsFromIds(List<Long> orderItemIds) {
        // Kiểm tra nếu danh sách ID là null hoặc rỗng
        if (orderItemIds == null || orderItemIds.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_ITEM_NOT_EXISTED); // Ném exception
        }

        // Lấy danh sách OrderItem từ repository
        List<OrderItem> orderItems = orderItemRepository.findAllById(orderItemIds);

        // Kiểm tra nếu không tìm thấy OrderItem nào
        if (orderItems.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_ITEM_NOT_EXISTED); // Ném exception
        }

        return orderItems;
    }

    public OrderBasicResponse createOrder(OrderRequest request) {
        // Kiểm tra User
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra Shipment
        Shipment shipment = shipmentRepository.findById(request.getShipmentId())
                .orElseThrow(() -> new AppException(ErrorCode.SHIPMENT_NOT_EXISTED));

        // Lấy danh sách sản phẩm hợp lệ từ orderItems
        List<OrderItem> orderItems = fetchOrderItemsFromIds(request.getOrderItemIds());

       //mapper
        Order newOrder=orderMapper.toOrder(request);
        if(newOrder.getStatus()==null){
            newOrder.setStatus(OrderStatusEnum.PENDING);
        }
        newOrder.setUser(user);
        newOrder.setShipment(shipment);
        // Liên kết các OrderItem với Order
        for (OrderItem item : orderItems) {
            item.setOrder(newOrder);
        }
        newOrder.setOrderItems(orderItems);

        // Chuyển đổi Order thành OrderResponse
        return mapToOrderBasicResponse(orderRepository.save(newOrder));
    }
    @PostAuthorize("returnObject.user.id == authentication.token.claims['userId']")
    public OrderDetailResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        return mapToOrderDetailResponse(order);
    }
    @PostAuthorize("returnObject.user.phoneNumber == authentication.name")
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        order.setStatus(OrderStatusEnum.CANCELED);
        orderRepository.save(order);
    }
    @PostAuthorize("returnObject.userId == authentication.token.claims['userId']")
    public OrderBasicResponse updateOrder(OrderRequest request, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        orderMapper.updateOrder(order,request);
        //kiểm tra user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra Shipment
        Shipment shipment = shipmentRepository.findById(request.getShipmentId())
                .orElseThrow(() -> new AppException(ErrorCode.SHIPMENT_NOT_EXISTED));
         // Lấy danh sách sản phẩm hợp lệ từ productId
        List<OrderItem> orderItems = fetchOrderItemsFromIds(request.getOrderItemIds());

        // Duy trì tham chiếu danh sách cũ và cập nhật
        List<OrderItem> currentOrderItem = order.getOrderItems();

        // Xóa các sản phẩm không còn trong danh sách mới
        currentOrderItem.removeIf(orderItem -> !orderItems.contains(orderItem));

        // Thêm các sản phẩm mới chưa có trong danh sách
        for (OrderItem orderItem : orderItems) {
            if (!currentOrderItem.contains(orderItem)) {
                orderItem.setOrder(order); // Cập nhật liên kết ngược
                currentOrderItem.add(orderItem);
            }
        }
        // Cập nhật Order
        order.setUser(user);
        order.setShipment(shipment);
        // Lưu Order
        Order updatedOrder = orderRepository.save(order);

        // Chuyển đổi Order thành OrderResponse thủ công
        return mapToOrderBasicResponse(order);
    }
    @PostAuthorize("returnObject.userId == authentication.token.claims['userId']")
    public OrderBasicResponse updateStatusOrder(OrderStatusEnum status,Long orderId){
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_EXISTED));
        order.setStatus(status);
        orderRepository.save(order);
        return mapToOrderBasicResponse(order);
    }


    public ResultPaginationDTO fetchAllOrders(Specification<Order> spec, Pageable pageable) {
        Page<Order> pageOrder = orderRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageOrder.getTotalPages());
        meta.setTotal(pageOrder.getTotalElements());

        rs.setMeta(meta); // Chuyển đổi từ Product sang ProductBasicResponse
        List<OrderBasicResponse> listOrder = pageOrder.getContent()
                .stream()
                .map(this::mapToOrderBasicResponse) // Sử dụng hàm mapToProductBasicResponse
                .collect(Collectors.toList());
        rs.setResult(listOrder);

        return rs;
    }
    @PreAuthorize("hasRole('USER')")
    public ResultPaginationDTO fetchAllOrdersByUser(Long userId, Pageable pageable) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        String currentUserPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!user.getPhoneNumber().equals(currentUserPhone)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        // Sử dụng Specification để tìm đơn hàng của user
        Page<Order> pageOrder = orderRepository.findAll(
                OrderSpecifications.hasUserId(userId),
                pageable
        );
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageOrder.getTotalPages());
        meta.setTotal(pageOrder.getTotalElements());
        rs.setMeta(meta);
        List<OrderBasicResponse> listOrder = pageOrder.getContent()
                .stream()
                .map(this::mapToOrderBasicResponse) // Ánh xạ Order sang OrderBasicResponse
                .collect(Collectors.toList());
        rs.setResult(listOrder);
        return rs;
    }
    public OrderDetailResponse mapToOrderDetailResponse(Order order) {
        // Chuyển đổi danh sách các sản phẩm trong order sang OrderItemBasicResponse
        List<OrderDetailResponse.OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(orderItem -> OrderDetailResponse.OrderItemResponse.builder()
                        .id(orderItem.getId())
                        .name(orderItem.getProduct().getName())
                        .numberOfProducts(orderItem.getNumberOfProducts())
                        .price(orderItem.getTotalMoney())
                        .build())
                .collect(Collectors.toList());

        // Chuyển đổi thông tin người dùng
        OrderDetailResponse.UserResponse userResponse = OrderDetailResponse.UserResponse.builder()
                .id(order.getUser().getId())
                .phoneNumber(order.getUser().getPhoneNumber())
                .email(order.getUser().getEmail())
                .build();

        // Chuyển đổi thông tin vận chuyển
        OrderDetailResponse.ShipmentResponse shipmentResponse = OrderDetailResponse.ShipmentResponse.builder()
                .id(order.getShipment().getId())
                .status(order.getShipment().getStatus())
                .build();

        // Xây dựng đối tượng OrderDetailResponse
        return OrderDetailResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .user(userResponse)
                .shipment(shipmentResponse)
                .status(order.getStatus())
                .orderItems(orderItemResponses)
                .build();
    }
    public OrderBasicResponse mapToOrderBasicResponse(Order order) {
        return OrderBasicResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .userId(order.getUser().getId())
                .shipmentId(order.getShipment().getId())
                .status(order.getStatus())
        .build();
    }



}
