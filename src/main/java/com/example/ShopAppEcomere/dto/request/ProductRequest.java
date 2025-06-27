package com.example.ShopAppEcomere.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String name_product;
    private String description;
    private Float price;
    private String img;
    private Integer quantity;
    private Integer category_id;
    private String brand;
    private Float promotionalPrice;
    private String descriptionShort;

}
