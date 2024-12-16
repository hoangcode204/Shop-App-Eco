package com.example.ShopAppEcomere.mapper;
import com.example.ShopAppEcomere.dto.request.ProductRequest;
import com.example.ShopAppEcomere.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true) // ID sẽ do database tự tạo
    @Mapping(target = "category", ignore = true) // Category được xử lý riêng trong service
    Product toProduct(ProductRequest request);

    @Mapping(target = "id", ignore = true) // ID không được thay đổi
    @Mapping(target = "category", ignore = true) // Category được xử lý riêng trong service
    void updateProduct(@MappingTarget Product product, ProductRequest request);
}
