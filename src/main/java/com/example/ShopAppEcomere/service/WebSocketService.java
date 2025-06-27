package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.response.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToAdmin(NotificationDTO notification) {
        // Đây là công cụ đặc biệt do Spring cung cấp để gửi tin nhắn
        // Hành động chính:
        // 1. Lấy đối tượng notification
        // 2. Chuyển nó thành JSON (tự động)
        // 3. Gửi đến kênh "/topic/admin-notifications"
        messagingTemplate.convertAndSend("/topic/admin-notifications", notification);
    }

    public void sendNewOrderNotification(Integer orderId, String customerName) {
        NotificationDTO notification = new NotificationDTO(

                "NEW_ORDER",
                "Có đơn hàng mới từ " + customerName,
                orderId,
                customerName,
                LocalDateTime.now(),
                "Chờ xác nhận"
        );
        sendNotificationToAdmin(notification);
    }

    public void sendCancelledOrderNotification(Integer orderId, String customerName) {
        NotificationDTO notification = new NotificationDTO(
                "CANCELLED_ORDER",
                "Đơn hàng #" + orderId + " đã được hủy bởi " + customerName,
                orderId,
                customerName,
                LocalDateTime.now(),
                "Đã hủy"
        );
        sendNotificationToAdmin(notification);
    }
}