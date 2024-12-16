package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.CartRequest;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.cart.CartBasicResponse;
import com.example.ShopAppEcomere.dto.response.cart.CartDetailResponse;
import com.example.ShopAppEcomere.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ApiResponse<CartBasicResponse> createCart(@Valid @RequestBody CartRequest request) {
        return ApiResponse.<CartBasicResponse>builder()
                .result(cartService.createCart(request))
                .build();
    }

    @PutMapping("/{cartId}")
    public ApiResponse<CartBasicResponse> updateCart(@PathVariable Long cartId, @RequestBody CartRequest request) {
        return ApiResponse.<CartBasicResponse>builder()
                .result(cartService.updateCart(cartId, request))
                .build();
    }

    @GetMapping("/{cartId}")
    public ApiResponse<CartDetailResponse> getCartById(@PathVariable Long cartId) {
        return ApiResponse.<CartDetailResponse>builder()
                .result(cartService.getCartById(cartId))
                .build();
    }

    @DeleteMapping("/{cartId}")
    public ApiResponse<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ApiResponse.<Void>builder()
                .message("Cart deleted successfully")
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<CartDetailResponse>> getAllCartsByUser(@PathVariable Long userId) {
        return ApiResponse.<List<CartDetailResponse>>builder()
                .result(cartService.getAllCartsByUser(userId))
                .build();
    }
}
