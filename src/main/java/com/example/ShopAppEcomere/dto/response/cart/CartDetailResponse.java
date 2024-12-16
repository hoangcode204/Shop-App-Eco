package com.example.ShopAppEcomere.dto.response.cart;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CartDetailResponse {

    private Long id;
    private int quantity;
    private Long userId;
    private ProductResponse product;
    @Getter
    @Setter
    @Builder
    public static class ProductResponse {
        private Long id;                 // ID của Product
        private String name;             // Tên sản phẩm
        private Float price;             // Giá sản phẩm
    }

}
