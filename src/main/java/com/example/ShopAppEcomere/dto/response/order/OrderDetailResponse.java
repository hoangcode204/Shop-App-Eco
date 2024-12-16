package com.example.ShopAppEcomere.dto.response.order;

import com.example.ShopAppEcomere.enums.OrderStatusEnum;
import com.example.ShopAppEcomere.enums.ShipmentStatusEnum;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private Date orderDate;
    private Integer totalPrice;
    private OrderStatusEnum status;
    private UserResponse user; // Thông tin người dùng
    private ShipmentResponse shipment; // Thông tin vận chuyển
    private List<OrderItemResponse> orderItems; // Danh sách sản phẩm

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        private Long id;
        private String phoneNumber;
        private String email;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShipmentResponse {
        private Long id;
        private ShipmentStatusEnum status; // Trạng thái vận chuyển
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Long id;
        private String name;
        private Integer  numberOfProducts;
        private Float price; // Giá mỗi sản phẩm
    }
}