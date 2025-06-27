package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.ReviewRequest;
import com.example.ShopAppEcomere.dto.response.review.ReviewResponse;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.entity.Review;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.ReviewMapper;
import com.example.ShopAppEcomere.repository.ProductRepository;
import com.example.ShopAppEcomere.repository.ReviewRepository;
import com.example.ShopAppEcomere.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<ReviewResponse> getAll(){
        List<Review> reviews=reviewRepository.findAll();
        return reviews.stream().map(reviewMapper::toReviewResponse)
                .collect(Collectors.toList());
    }
    public ReviewResponse create(ReviewRequest reviewRequest) {
        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setCreated_at(LocalDateTime.now());
        review.setRating(reviewRequest.getRating()); // ⭐ Thêm dòng này
        // Lấy Account từ userId
        User user = userRepository.findById(reviewRequest.getUser_id())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        review.setUser(user);
        // Lấy Product từ productId
        Product product = productRepository.findById(reviewRequest.getProduct_id())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        review.setProduct(product);
        return reviewMapper.toReviewResponse( reviewRepository.save(review));
    }
    public List<ReviewResponse> getReviewsByProductId(Integer id) {
        List<Review> reviewList=reviewRepository.getReviewByProductId(id);
        return reviewList.stream().map(reviewMapper::toReviewResponse)
                .collect(Collectors.toList());
    }
    public List<Integer> getReviewedProductIdsByUserId(Integer userId) {
        List<Review> reviews = reviewRepository.getReviewsByUserId(userId);
        return reviews.stream()
                .map(r -> r.getProduct().getId())
                .collect(Collectors.toList());
    }
}
