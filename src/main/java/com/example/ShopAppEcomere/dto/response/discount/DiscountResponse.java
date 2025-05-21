package com.example.ShopAppEcomere.dto.response.discount;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponse {
    private Integer id;

    private  String name;

    private String description;

    private Integer discount_percent;

    private Boolean is_active;
}
