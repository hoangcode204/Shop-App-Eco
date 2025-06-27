package com.example.ShopAppEcomere.mapper;
import com.example.ShopAppEcomere.dto.response.review.ReviewResponse;
import com.example.ShopAppEcomere.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

   @Mapping(source = "product.id", target = "product_id")
   @Mapping(source = "user", target = "user") // Sử dụng UserMapper để map
   @Mapping(source = "rating", target = "rating") // ⭐️ Thêm dòng này
   ReviewResponse toReviewResponse(Review review);
}