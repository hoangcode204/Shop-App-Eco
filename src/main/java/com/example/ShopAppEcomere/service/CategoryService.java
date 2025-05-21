package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.response.category.CategoryResponse;
import com.example.ShopAppEcomere.entity.Category;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.CategoryMapper;
import com.example.ShopAppEcomere.repository.CategoryRepository;
import com.example.ShopAppEcomere.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryResponse save(Category categoryRequest) {
        if(categoryRepository.existsByName(categoryRequest.getName())){
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
       return categoryMapper.toCategoryResponse(categoryRepository.save(categoryRequest));
    }


    public CategoryResponse update(Category categoryRequest, Integer id) {
        Category updateCategory = categoryRepository.getCategoryById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        updateCategory.setName(categoryRequest.getName());
        updateCategory.setDescription(categoryRequest.getDescription());
        updateCategory.setUpdatedAt(categoryRequest.getUpdatedAt());
        return categoryMapper.toCategoryResponse(categoryRepository.save(updateCategory));
    }


    public CategoryResponse deleted(Integer id) {
        Category category = categoryRepository.getCategoryById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        category.setDeletedAt(new Date());
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }
    public List<CategoryResponse> getAllCategory() {
        List<Category> categories = categoryRepository.getAllCategory();
        return categories.stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }
    public CategoryResponse getCategoryById(Integer id){
        Category category = categoryRepository.getCategoryById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        return categoryMapper.toCategoryResponse(category);
    }

}
