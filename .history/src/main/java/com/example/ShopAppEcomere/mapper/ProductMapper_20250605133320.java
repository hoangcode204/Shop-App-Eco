package com.example.ShopAppEcomere.mapper;
import com.example.ShopAppEcomere.dto.request.ProductRequest;
import com.example.ShopAppEcomere.dto.response.product.ProductResponse;
import com.example.ShopAppEcomere.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", source = "brand")
    Product toProduct(ProductRequest productRequest);

    @Mapping(target = "category", source = "category")
    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "specifications", source = "specifications", qualifiedByName = "toSpecificationRequests")
    ProductResponse toProductResponse(Product product);

    @Named("toSpecificationRequests")
    default List<SpecificationRequest> toSpecificationRequests(List<ProductSpecification> specifications) {
        if (specifications == null) {
            return null;
        }
        return specifications.stream()
                .map(spec -> new SpecificationRequest(spec.getKey(), spec.getValue()))
                .collect(Collectors.toList());
    }
}
