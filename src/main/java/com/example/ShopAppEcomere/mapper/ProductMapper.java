package com.example.ShopAppEcomere.mapper;
import com.example.ShopAppEcomere.dto.request.ProductRequest;
import com.example.ShopAppEcomere.dto.response.product.ProductResponse;
import com.example.ShopAppEcomere.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "promotionalPrice", source = "promotionalPrice")
    @Mapping(target = "descriptionShort", source = "descriptionShort")
    Product toProduct(ProductRequest productRequest);

    @Mapping(target = "category", source = "category")
    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "promotionalPrice", source = "promotionalPrice")
    @Mapping(target = "descriptionShort", source = "descriptionShort")
    ProductResponse toProductResponse(Product product);


}
