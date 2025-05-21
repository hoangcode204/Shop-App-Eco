package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.entity.OrderAccount;
import com.example.ShopAppEcomere.repository.OrderAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderAccService {
    private final OrderAccountRepository orderAccount;

    public List<OrderAccount> getOrderByAcc(Integer idacc) {
        return orderAccount.getOrderByAcc(idacc);
    }

    public List<OrderAccount> getOrderByAccAndStatus(Integer idacc, Integer status) {
        return orderAccount.getOrderByAccAndStatus(idacc, status);

    }
}
