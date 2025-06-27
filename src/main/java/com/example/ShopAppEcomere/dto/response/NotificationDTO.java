package com.example.ShopAppEcomere.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String type; // "NEW_ORDER", "CANCELLED_ORDER"
    private String message;
    private Integer orderId;
    private String customerName;
    private LocalDateTime timestamp;
    private String status;
}