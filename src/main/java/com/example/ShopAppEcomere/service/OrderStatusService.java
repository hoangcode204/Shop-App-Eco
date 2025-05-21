package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.entity.OrderStatus;
import com.example.ShopAppEcomere.repository.OrderRepository;
import com.example.ShopAppEcomere.repository.OrderStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderStatusService {
    private final OrderStatusRepository orderStatusRepository;
    public OrderStatus findOrderbyId(Integer id){
        return orderStatusRepository.findByOrderById(id).orElse(null);
    }
}
