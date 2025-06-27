package com.example.ShopAppEcomere.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Tạo ra một cổng vào cho kết nối WebSocket ban đầu
        // Frontend sẽ kết nối tới "http://your-server/ws"
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Cho phép mọi frontend kết nối
                .withSockJS(); // Fallback nếu trình duyệt không hỗ trợ WebSocket
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Định nghĩa các "kênh" mà server có thể gửi tin đi
        // /topic: Dành cho thông báo công cộng (một-nhiều)
        // /queue: Dành cho thông báo riêng tư (một-một)
        registry.enableSimpleBroker("/topic", "/queue");

        // Định nghĩa tiền tố cho các tin nhắn từ client gửi đến server
        // Nếu client gửi đến "/app/admin/connect", nó sẽ được định tuyến
        // đến method @MessageMapping("/admin/connect")
        registry.setApplicationDestinationPrefixes("/app");
    }
}