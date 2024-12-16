package com.example.ShopAppEcomere.dto.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDetailResponse {
    private Long id;
    private String name;
    private Float price;
    private String thumbnail;
    private String description;
    private Integer stock;
    private CategoryResponse category; //  trả về thông tin danh mục
    private Boolean active;

    @Getter
    @Setter
    @Builder
    public static class CategoryResponse {
        private Long id;
        private String name;
    }
}