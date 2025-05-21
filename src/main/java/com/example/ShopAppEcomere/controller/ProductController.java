package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.ProductRequest;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.product.ProductResponse;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.service.ProductService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping("/products")
    @ApiMessage("Create a new product")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<ProductResponse> postSave(@RequestPart("product") ProductRequest request, MultipartFile file){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.create(request,file))
                .build();
    }
    @GetMapping("/products/{productId}")
    @ApiMessage("Fetch product by id")
    public ApiResponse<ProductResponse> getProductById(@PathVariable("productId") Integer productId) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductById(productId))
                .build();
    }


    @PutMapping("/products/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiMessage("Update product")
    public ApiResponse<?> updateProduct(@PathVariable("productId") Integer productId,@RequestPart("product") ProductRequest request, MultipartFile file){
        return  ApiResponse.<ProductResponse>builder()
                .result(productService.update(productId,request,file))
                .build();
    }
    @DeleteMapping("/products/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiMessage("Delete product")
    public ApiResponse<ProductResponse> deleteProduct(@PathVariable("productId") Integer productId) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.deleteProductById(productId))
                .message("Delete product successful")
                .build();
    }
@GetMapping("/products")
public ApiResponse<Page<ProductResponse>> getProducts(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "limit", defaultValue = "20") int limit,
        @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "order", defaultValue = "desc") String order,
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "category", required = false) String category,
        @RequestParam(name = "price_max", required = false) Float priceMax,
        @RequestParam(name = "price_min", required = false) Float priceMin) {

    Page<ProductResponse> products = productService.getFilteredProducts(page, limit, sortBy, name, category, priceMax, priceMin, order);

    return ApiResponse.<Page<ProductResponse>>builder()
            .result(products)
            .build();
}
}
