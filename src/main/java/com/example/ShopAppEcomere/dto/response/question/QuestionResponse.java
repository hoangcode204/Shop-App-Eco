package com.example.ShopAppEcomere.dto.response.question;

import com.example.ShopAppEcomere.dto.response.UserResponse;
import com.example.ShopAppEcomere.dto.response.product.ProductResponse;
import com.example.ShopAppEcomere.enums.QuestionStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionResponse {
    Integer id;
    String questionText;
    String answerText;
    QuestionStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UserResponse user;
    UserResponse admin;
    ProductResponse product;
}