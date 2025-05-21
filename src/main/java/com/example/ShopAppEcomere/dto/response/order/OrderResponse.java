package com.example.ShopAppEcomere.dto.response.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private Integer totalPrice;
    private String fullname;
    private String phone;
    private String city;
    private String district;
    private String wards;
    private String specific_address;
   private OrderStatusResponse status;
}