package com.example.ShopAppEcomere.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/admin/connect") // Đây là endpoint mà client sẽ gửi tin nhắn đến
    @SendTo("/topic/admin-status")   // Đây là kênh mà server sẽ gửi phản hồi đến
    public String adminConnected(String message) {
        return "Admin connected: " + message;
    }
}