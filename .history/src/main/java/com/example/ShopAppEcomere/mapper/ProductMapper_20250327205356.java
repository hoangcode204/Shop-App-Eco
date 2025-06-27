package com.example.ShopAppEcomere.mapper;
import com.example.ShopAppEcomere.dto.request.ProductRequest;
import com.example.ShopAppEcomere.dto.response.product.ProductResponse;
import com.example.ShopAppEcomere.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    Product toProduct(ProductRequest productRequest);
    @Mapping(target = "category", source = "category")
    ProductResponse toProductResponse(Product product);
}
