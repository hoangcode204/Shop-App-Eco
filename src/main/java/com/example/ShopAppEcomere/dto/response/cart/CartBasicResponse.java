package com.example.ShopAppEcomere.dto.response.cart;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CartBasicResponse {

    private Long id;
    private int quantity;
    private Long userId;
    private Long productId;

}
