package com.example.ShopAppEcomere.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class OrderItemRequest {

    @NotNull(message = "Number of products is required")
    @Positive(message = "Number of products must be greater than 0")
    private int numberOfProducts;

    @NotNull(message = "Total money is required")
    @Positive(message = "Total money must be greater than 0")
    private Float totalMoney;
    private String color;
    @NotNull(message = "Order ID is required")
    private Long orderId;
    @NotNull(message = "Product ID is required")
    private Long productId;

}
