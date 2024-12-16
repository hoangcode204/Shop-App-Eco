package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.CategoryRequest;
import com.example.ShopAppEcomere.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // Chuyển đổi từ CategoryRequest sang Category
    @Mapping(target = "products", ignore = true) // Bỏ qua mapping trường products
    Category toCategory(CategoryRequest request);

//    // Chuyển đổi từ Category sang CategoryResponse
//    @Mapping(target = "productList", source = "products") // Map products sang productList
//    CategoryResponse toCategoryResponse(Category category);

    // Cập nhật dữ liệu từ CategoryRequest vào Category đã tồn tại
    @Mapping(target = "products", ignore = true) // Bỏ qua cập nhật products
    void updateCategory(@MappingTarget Category category, CategoryRequest request);
}
