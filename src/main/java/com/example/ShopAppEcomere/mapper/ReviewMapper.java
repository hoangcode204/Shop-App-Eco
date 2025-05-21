package com.example.ShopAppEcomere.mapper;
import com.example.ShopAppEcomere.dto.response.review.ReviewResponse;
import com.example.ShopAppEcomere.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

   @Mapping(source = "user.id", target = "user_id")  // Lấy ID của Account
   @Mapping(source = "product.id", target = "product_id")  // Lấy ID của Product
   ReviewResponse toReviewResponse(Review review);
}
