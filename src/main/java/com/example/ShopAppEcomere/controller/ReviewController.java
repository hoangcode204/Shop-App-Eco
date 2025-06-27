package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.ReviewRequest;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.review.ReviewResponse;
import com.example.ShopAppEcomere.entity.Review;
import com.example.ShopAppEcomere.service.ReviewService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping
    @ApiMessage("Fetch all review ")
    public ApiResponse<List<ReviewResponse>> getAllReview() {
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getAll())
                .build();
    }
    @PostMapping
    @ApiMessage("Post a new review ")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ApiResponse<ReviewResponse> postReview(@RequestBody ReviewRequest reviewRequest) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.create(reviewRequest))
                .build();
    }
    @GetMapping("/{productId}")
    @ApiMessage("Get review by Id")
    public ApiResponse<List<ReviewResponse>> getReviewById(@PathVariable("productId") Integer productId) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getReviewsByProductId(productId))
                .build();
    }
    @GetMapping("/user/{userId}/product-ids")
    public ApiResponse<List<Integer>> getReviewedProductIdsByUserId(@PathVariable("userId") Integer userId) {
        return ApiResponse.<List<Integer>>builder()
                .result(reviewService.getReviewedProductIdsByUserId(userId))
                .build();
    }




}
