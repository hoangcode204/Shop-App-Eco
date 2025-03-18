package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.CategoryRequest;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.category.CategoryDetailResponse;
import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.entity.Category;
import com.example.ShopAppEcomere.service.CategoryService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/categories")
    @ApiMessage("Create a new category")
    public ApiResponse<CategoryDetailResponse> createCategory(@Valid @RequestBody CategoryRequest request){
        return ApiResponse.<CategoryDetailResponse>builder()
                .result(categoryService.createCategory(request))
                .build();
    }
    @GetMapping("/categories/{categoryId}")
    @ApiMessage("Fetch category by id")
    public ApiResponse<CategoryDetailResponse> getCategoryById(@PathVariable("categoryId") @Min(1) Long categoryId) {
        return ApiResponse.<CategoryDetailResponse>builder()
                .result(categoryService.getCategoryById(categoryId))
                .build();
    }
    @GetMapping("/categories")
    @ApiMessage("fetch all Category")
    public ApiResponse<ResultPaginationDTO> getAllCategory(
            @Filter Specification<Category> spec,
            Pageable pageable) {
        return ApiResponse.<ResultPaginationDTO>builder()
                .result(this.categoryService.fetchAllCategory(spec, pageable))
                .build();

    }

    @PutMapping("/categories/{categoryId}")
    @ApiMessage("Update category")
    public ApiResponse<CategoryDetailResponse> updateCategory(@PathVariable @Min(1) Long categoryId, @RequestBody CategoryRequest request){
        return  ApiResponse.<CategoryDetailResponse>builder()
                .result(categoryService.updateCategory(request,categoryId))
                .build();
    }
    @DeleteMapping("/categories/{categoryId}")
    @ApiMessage("Delete Category")
    public ApiResponse<Void> deleteCategory(@PathVariable("categoryId") @Min(1) Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.<Void>builder()
                .message("Delete category successful")
                .build();
    }

}
