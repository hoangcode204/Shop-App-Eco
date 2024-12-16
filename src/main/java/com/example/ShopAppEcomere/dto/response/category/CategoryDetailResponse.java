package com.example.ShopAppEcomere.dto.response.category;
import com.example.ShopAppEcomere.entity.Product;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDetailResponse {
    private Long id;
    private String name;
    private List<ProductBasicResponse> products;
    private Boolean active;
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ProductBasicResponse {
        private Long id;
        private String name;
        private Boolean active;

    }

}
