package com.example.ShopAppEcomere.dto.response.orderItem;

import com.example.ShopAppEcomere.enums.OrderStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderItemDetailResponse {
    private Long id;                     // ID của Order Item
    private int numberOfProducts;        // Số lượng sản phẩm
    private Float totalMoney;            // Tổng tiền
    private String color;                // Màu sắc sản phẩm
    private LocalDateTime createdAt;     // Ngày tạo
    private LocalDateTime updatedAt;     // Ngày cập nhật
    private OrderResponse order;         // Thông tin chi tiết về Order

    private ProductResponse product;     // Thông tin chi tiết về Product

    @Getter
    @Setter
    @Builder
    public static class OrderResponse {
        private Long id;                 // ID của Order
        private OrderStatusEnum status;           // Trạng thái của Order
        private Long userId;
    }

    @Getter
    @Setter
    @Builder
    public static class ProductResponse {
        private Long id;                 // ID của Product
        private String name;             // Tên sản phẩm
        private Float price;             // Giá sản phẩm
    }
}