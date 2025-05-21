package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.StripeCheckoutDTO;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.service.StripeService;
import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stripe")
@AllArgsConstructor
public class StripeController {
    private final StripeService stripeService;
    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('ROLE_USER')or hasAuthority('ROLE_ADMIN')")
    public ApiResponse<?> checkout(@RequestBody StripeCheckoutDTO stripeCheckoutDTO) throws StripeException {
        return ApiResponse.builder()
                .result(stripeService.checkout(stripeCheckoutDTO))
                .build();
    }
}
