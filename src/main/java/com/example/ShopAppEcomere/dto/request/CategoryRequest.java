package com.example.ShopAppEcomere.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "name không được để trống")
    private String name;
    @Min(value = 1,message = "ProductId must than 1")
    private List<Long> productId;
    private Boolean active=true;
}
