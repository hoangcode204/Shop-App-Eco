package com.example.ShopAppEcomere.dto.request;

import com.example.ShopAppEcomere.enums.OrderStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "orderDate không được để trống")
    @PastOrPresent(message = "orderDate phải là ngày trong quá khứ hoặc hiện tại")
    private Date orderDate;
    @Positive(message = "price phải là số dương")
    private Integer totalPrice;
    private Long userId;
    private Long shipmentId;
    private OrderStatusEnum status;
    private List<Long> orderItemIds;
}
