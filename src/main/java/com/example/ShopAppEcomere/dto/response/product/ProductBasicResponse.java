package com.example.ShopAppEcomere.dto.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductBasicResponse {
    private Long id;
    private String name;
    private Float price;
    private String thumbnail;
    private Integer stock;
    private Boolean active;
}