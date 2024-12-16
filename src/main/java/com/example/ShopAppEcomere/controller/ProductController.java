package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.ProductRequest;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.product.ProductDetailResponse;
import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.service.ProductService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping("/products")
    @ApiMessage("Create a new product")
    public ApiResponse<ProductDetailResponse> createProduct(@Valid @RequestBody ProductRequest request){
        return ApiResponse.<ProductDetailResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }
    @GetMapping("/products/{productId}")
    @ApiMessage("Fetch product by id")
    public ApiResponse<ProductDetailResponse> getProductById(@PathVariable("productId") Long productId) {
        return ApiResponse.<ProductDetailResponse>builder()
                .result(productService.getProductById(productId))
                .build();
    }
    @GetMapping("/products")
    @ApiMessage("fetch all Product")
    public ApiResponse<ResultPaginationDTO> getAllProduct(
            @Filter Specification<Product> spec,
            Pageable pageable) {
        return ApiResponse.<ResultPaginationDTO>builder()
                .result(this.productService.fetchAllProduct(spec, pageable))
                .build();

    }

    @PutMapping("/products/{productId}")
    @ApiMessage("Update product")
    public ApiResponse<ProductDetailResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductRequest request){
        return  ApiResponse.<ProductDetailResponse>builder()
                .result(productService.updateProduct(request,productId))
                .build();
    }
    @DeleteMapping("/products/{productId}")
    @ApiMessage("Delete product")
    public ApiResponse<Void> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<Void>builder()
                .message("Delete product successful")
                .build();
    }

}
