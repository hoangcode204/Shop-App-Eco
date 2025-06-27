package com.example.ShopAppEcomere.dto.response.order;

import com.example.ShopAppEcomere.dto.response.discount.DiscountResponse;
import com.example.ShopAppEcomere.dto.response.orderItem.OrderItemResponse;
import com.example.ShopAppEcomere.entity.Discount;
import com.example.ShopAppEcomere.entity.OrderItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Integer id;
    private Integer totalPrice;
    private String fullname;
    private String phone;
    private String city;
    private String district;
    private String wards;
    private String specific_address;
    private OrderStatusResponse status;
    private List<OrderItemResponse> orderItems;
    private DiscountResponse discount;
    private String vnpTxnRef;
    private Boolean isPaid = false;
}