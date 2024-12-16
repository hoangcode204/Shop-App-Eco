package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.OrderItemRequest;
import com.example.ShopAppEcomere.dto.response.order.OrderBasicResponse;
import com.example.ShopAppEcomere.dto.response.orderItem.OrderItemBasicResponse;
import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.dto.response.orderItem.OrderItemDetailResponse;
import com.example.ShopAppEcomere.entity.Order;
import com.example.ShopAppEcomere.entity.OrderItem;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.OrderItemMapper;
import com.example.ShopAppEcomere.repository.OrderItemRepository;
import com.example.ShopAppEcomere.repository.OrderRepository;
import com.example.ShopAppEcomere.repository.ProductRepository;
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
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemDetailResponse createOrderItem(OrderItemRequest request) {
        //check order
        Order order=orderRepository.findById(request.getOrderId())
                .orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_EXISTED));
        //check product
        Product product=productRepository.findById(request.getProductId())
                .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        OrderItem newOrderItem = orderItemMapper.toOrderItem(request);
        newOrderItem.setOrder(order);
        newOrderItem.setProduct(product);
        return toOrderItemDetailResponse(orderItemRepository.save(newOrderItem));
    }
    @PostAuthorize("returnObject.order.userId == authentication.token.claims['userId']")
    public OrderItemDetailResponse getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_ITEM_NOT_EXISTED));

        // Kiểm tra số lượng sản phẩm trước khi tiếp tục
        if (orderItem.getNumberOfProducts() > 0) {
            return toOrderItemDetailResponse(orderItem);
        } else {
            // Nếu numberOfProducts <= 0, trả về null hoặc xử lý theo logic khác
            throw new AppException(ErrorCode.ORDER_ITEM_NOT_EXISTED);
        }

    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteOrderItem(Long orderItemId) throws AccessDeniedException {
        OrderItem orderItem=orderItemRepository.findById(orderItemId)
                        .orElseThrow(()->new AppException(ErrorCode.ORDER_ITEM_NOT_EXISTED));
        // Kiểm tra nếu phone người dùng hiện tại không khớp với phone của user trong payment
        String currentUserPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Authorities: "+currentUserPhone );
        if (!orderItem.getOrder().getUser().getPhoneNumber().equals(currentUserPhone)) {
            throw new AccessDeniedException("Bạn không có quyền xóa đơn hàng này");
        }
        orderItem.setNumberOfProducts(orderItem.getNumberOfProducts()-1);
        orderItemRepository.save(orderItem);
    }
    @PostAuthorize("returnObject.order.userId == authentication.token.claims['userId']")
    public OrderItemDetailResponse updateOrderItem(OrderItemRequest request, Long orderItemId) {

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_ITEM_NOT_EXISTED));
        //check order
        Order order=orderRepository.findById(request.getOrderId())
                .orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_EXISTED));
        //check product
        Product product=productRepository.findById(request.getProductId())
                .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        orderItemMapper.updateOrderItem(orderItem, request);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        return toOrderItemDetailResponse(orderItemRepository.save(orderItem));
    }

    public ResultPaginationDTO fetchAllOrderItems(Specification<OrderItem> spec, Pageable pageable) {
        Page<OrderItem> pageOrderItem = orderItemRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageOrderItem.getTotalPages());
        meta.setTotal(pageOrderItem.getTotalElements());

        rs.setMeta(meta);

        List<OrderItemBasicResponse> listOrder = pageOrderItem.getContent()
                .stream()
                .map(this::toOrderItemBasicResponse) // Sử dụng hàm mapToProductBasicResponse
                .collect(Collectors.toList());

        rs.setResult(listOrder);
        return rs;
    }
    public OrderItemBasicResponse toOrderItemBasicResponse(OrderItem orderItem) {
        return OrderItemBasicResponse.builder()
                .id(orderItem.getId())
                .numberOfProducts(orderItem.getNumberOfProducts())
                .totalMoney(orderItem.getTotalMoney())
                .build();
    }
    public OrderItemDetailResponse toOrderItemDetailResponse(OrderItem orderItem) {
        return OrderItemDetailResponse.builder()
                .id(orderItem.getId())
                .numberOfProducts(orderItem.getNumberOfProducts())
                .totalMoney(orderItem.getTotalMoney())
                .color(orderItem.getColor())
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .order(OrderItemDetailResponse.OrderResponse.builder()
                        .id(orderItem.getOrder().getId())
                        .status(orderItem.getOrder().getStatus())
                        .userId(orderItem.getOrder().getUser().getId())
                        .build())
                .product(OrderItemDetailResponse.ProductResponse.builder()
                        .id(orderItem.getProduct().getId())
                        .name(orderItem.getProduct().getName())
                        .price(orderItem.getProduct().getPrice())
                        .build())
                .build();
    }


}
