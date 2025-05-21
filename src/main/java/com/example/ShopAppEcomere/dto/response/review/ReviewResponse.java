package com.example.ShopAppEcomere.dto.response.review;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Integer id;

    private String comment;
    private LocalDateTime created_at;
    private Integer user_id;
    private Integer product_id;
}
