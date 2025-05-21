package com.example.ShopAppEcomere.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private String comment;
    private LocalDateTime created_at;
    private Integer user_id;
    private Integer product_id;
}
