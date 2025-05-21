package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.OrderItem.OrderItemDTO;
import com.example.ShopAppEcomere.dto.response.orderItem.OrderItemResponse;
import com.example.ShopAppEcomere.entity.OrderItem;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.OrderItemMapper;
import com.example.ShopAppEcomere.repository.OrderItemRepository;
import com.example.ShopAppEcomere.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ProductRepository productRepository;


    public List<OrderItemResponse> getAll(){
        List<OrderItem>orderItems=orderItemRepository.findAll();
        return orderItems.stream().map(orderItemMapper::toOrderItemResponse)
                .collect(Collectors.toList());
    }
    public OrderItemResponse createOrderDetails(OrderItemDTO request){
        Product product=productRepository.findProductById(request.getProductId());
        if(product==null){
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        OrderItem orderItem=new OrderItem();
        orderItem.setQuantity(request.getQuantity());
        orderItem.setProduct(product);
        orderItem.setAmount(product.getPrice()*request.getQuantity());
        orderItemRepository.save(orderItem);
        return orderItemMapper.toOrderItemResponse(orderItem);
    }


}
