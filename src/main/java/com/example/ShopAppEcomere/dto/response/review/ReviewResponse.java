package com.example.ShopAppEcomere.dto.response.review;

import com.example.ShopAppEcomere.dto.response.UserResponse;
import com.example.ShopAppEcomere.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Integer id;
    private Integer rating; // ⭐ Thêm dòng này
    private String comment;
    private LocalDateTime created_at;
    private UserResponse user;
    private Integer product_id;
}
