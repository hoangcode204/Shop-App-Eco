package com.example.ShopAppEcomere.dto.request.order;

import com.example.ShopAppEcomere.dto.request.OrderItem.OrderItemDTO;
import com.example.ShopAppEcomere.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String fullname;
    private String phone;
    private String city;
    private String district;
    private String wards;
    private String specificAddress;
    private List<OrderItemDTO> orderDetails;
    private Integer userId;
    private Integer discountId;
}