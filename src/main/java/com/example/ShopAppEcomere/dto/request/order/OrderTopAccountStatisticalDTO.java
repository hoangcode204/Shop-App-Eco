package com.example.ShopAppEcomere.dto.request.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTopAccountStatisticalDTO {
    private Integer id;
    private String email;
    private String fullname;
    private String img;
    private Long totalAmount;
}
