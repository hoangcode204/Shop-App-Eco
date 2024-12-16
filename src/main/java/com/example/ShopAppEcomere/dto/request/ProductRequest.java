package com.example.ShopAppEcomere.dto.request;

import com.example.ShopAppEcomere.entity.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 255,message = "Giới hạn 255 kí tự")
    private String name;

    @NotNull()
    @Positive(message = "price phải là số dương")
    private Float price;

    @Size(max = 500, message = "THUMBNAIL_TOO_LONG")
    private String thumbnail;

    @Size(max = 1000, message = "DESCRIPTION_TOO_LONG")
    private String description;

    @NotNull(message = "STOCK_REQUIRED")
    @Min(value = 0, message = "Stock phải là số dương")
    private Integer stock;
    private Long categoryId;
    private Boolean active;
}
