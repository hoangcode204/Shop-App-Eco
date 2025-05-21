package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.category.CategoryResponse;
import com.example.ShopAppEcomere.entity.Category;
import com.example.ShopAppEcomere.repository.CategoryRepository;
import com.example.ShopAppEcomere.service.CategoryService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/categories")
    @ApiMessage("Fetch all categories")
    public ApiResponse<List<CategoryResponse>> getAllCategory() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getAllCategory())
                .build();
    }
    @GetMapping("/categories/{id}")
    @ApiMessage("Fetch category by Id")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.getCategoryById(id))
                .build();
    }
    @PostMapping("/categories")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiMessage("Create new category")
    public ApiResponse<CategoryResponse> create( @RequestBody Category categoryRequest) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.save(categoryRequest))
                .build();
    }

    @PutMapping("/categories/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiMessage("Update category")
        public ApiResponse<CategoryResponse> update( @RequestBody Category categoryRequest,@PathVariable Integer id){
            return ApiResponse.<CategoryResponse>builder()
                    .result(categoryService.update(categoryRequest,id))
                    .build();
        }
    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiMessage("Delete category by id")
    public ApiResponse<CategoryResponse> delete(@PathVariable Integer id){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.deleted(id))
                .build();
    }
}
