package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.entity.Notification;
import com.example.ShopAppEcomere.entity.Order;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.repository.NotificationRepository;
import com.example.ShopAppEcomere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void createNotificationForAdmins(Order order, String message) {
        List<User> admins = userRepository.findByRoleName("ADMIN");
        for (User admin : admins) {
            Notification notification = Notification.builder()
                    .recipient(admin)
                    .order(order)
                    .message(message)
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(notification);
        }
    }

    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findByRecipientAndIsReadFalseOrderByCreatedAtDesc(user);
    }

    public Notification markAsRead(Long notificationId, User user) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getRecipient().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        notification.setRead(true);
        return notificationRepository.save(notification);
    }
}