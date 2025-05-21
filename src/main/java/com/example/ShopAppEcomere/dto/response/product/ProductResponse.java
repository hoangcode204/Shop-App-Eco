package com.example.ShopAppEcomere.dto.response.product;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Integer id;

    private String name_product;

    private String description;

    private Integer price;

    private String img;

    private Integer quantity;
    private CategoryBasicResponse category;
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryBasicResponse{
        private Long id;
        private String name;
    }
}
