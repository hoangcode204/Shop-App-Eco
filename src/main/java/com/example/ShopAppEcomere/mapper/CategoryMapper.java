package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.response.category.CategoryResponse;
import com.example.ShopAppEcomere.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // Chuyển đổi từ Category sang CategoryResponse
    CategoryResponse toCategoryResponse(Category category);

}
