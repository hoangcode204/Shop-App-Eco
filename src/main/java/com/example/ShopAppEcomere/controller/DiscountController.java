package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.discount.DiscountResponse;
import com.example.ShopAppEcomere.entity.Discount;
import com.example.ShopAppEcomere.service.DiscountService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discounts")
public class DiscountController {
    private final DiscountService discountService;
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @ApiMessage("Fetch all discounts")
    public ApiResponse<List<DiscountResponse>> getAllDiscounts() {
        return ApiResponse.<List<DiscountResponse>>builder()
                .result(discountService.getAllDiscounts())
                .build();
    }
    @GetMapping("/{id}")
    @ApiMessage("Fetch discount by ID")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ApiResponse<DiscountResponse> getDiscountById(@PathVariable Integer id) {
        return ApiResponse.<DiscountResponse>builder()
                .result(discountService.getDiscountById(id))
                .build();
    }
    @PostMapping
    @ApiMessage("Create new discount")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<DiscountResponse> postSave( @RequestBody Discount discountRequest) {
        return ApiResponse.<DiscountResponse>builder()
                .result(discountService.create(discountRequest))
                .build();
    }

    @PutMapping("/{id}")
    @ApiMessage("Update discount")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<DiscountResponse> updateDiscountById( @RequestBody Discount discountRequest, @PathVariable Integer id) {
        return ApiResponse.<DiscountResponse>builder()
                .result(discountService.update(discountRequest, id))
                .build();
    }


}
