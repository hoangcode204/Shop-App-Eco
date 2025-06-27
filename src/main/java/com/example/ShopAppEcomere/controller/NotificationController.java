package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.NotificationResponse;
import com.example.ShopAppEcomere.entity.Notification;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.mapper.NotificationMapper;
import com.example.ShopAppEcomere.service.NotificationService;
import com.example.ShopAppEcomere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;
    private final NotificationMapper notificationMapper;

    @GetMapping("/unread")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<NotificationResponse>> getUnreadNotifications() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.getUserByUsername(username);

        List<Notification> notifications = notificationService.getUnreadNotifications(currentUser);
        List<NotificationResponse> response = notifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<NotificationResponse>>builder().result(response).build();
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<NotificationResponse> markAsRead(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.getUserByUsername(username);

        Notification notification = notificationService.markAsRead(id, currentUser);
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationMapper.toNotificationResponse(notification))
                .build();
    }
}