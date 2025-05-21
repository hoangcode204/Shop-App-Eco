package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.PasswordCreationRequest;
import com.example.ShopAppEcomere.dto.request.UserRequest;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.UserResponse;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.service.EmailService;
import com.example.ShopAppEcomere.service.UserService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/users")

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;
    EmailService emailService;

    @PostMapping(value = "/register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<UserResponse> createUser(@RequestPart("user")  UserRequest request, @RequestPart("image") MultipartFile file){
        UserResponse userResponse = userService.createUser(request, file);
        // Gửi email thông báo đăng ký thành công (chạy bất đồng bộ)
        sendRegistrationEmail(request.getEmail(), request.getUsername());
        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }
//    private void sendRegistrationEmail(String email, String username) {
//        CompletableFuture.runAsync(() -> {
//            try {
//                String subject = "Đăng ký tài khoản thành công";
//                String content = String.format(
//                        "Chào %s,\n\nCảm ơn bạn đã đăng ký tài khoản tại cửa hàng của chúng tôi!\n" +
//                                "Tài khoản của bạn đã được tạo thành công. Nếu có bất kỳ câu hỏi hoặc cần hỗ trợ, vui lòng liên hệ với chúng tôi.\n\n" +
//                                "Trân trọng,\nCửa hàng của chúng tôi.",
//                        username
//                );
//
//                emailService.sendEmail(subject, email, content);
//                System.out.println(" Email đăng ký đã gửi thành công!");
//            } catch (Exception e) {
//                System.err.println("Lỗi khi gửi email: " + e.getMessage());
//            }
//        });
//    }
private void sendRegistrationEmail(String email, String username) {
    CompletableFuture.runAsync(() -> {
        try {
            String subject = "🎉 Chào mừng bạn đến với cửa hàng của chúng tôi! 🎉";
            String content = String.format(
                    "<html>" +
                            "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                            "<div style='max-width: 600px; background: white; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1);'>" +
                            "<div style='text-align: center;'>" +
                            "<img src='http://localhost:8081/Huy-Hoang.png' alt='Logo' " +
                            "style='width: 150px; height: 150px; margin-bottom: 20px;'/>" +
                            "</div>" +
                            "<h2 style='color: #333;'>Xin chào %s,</h2>" +
                            "<p style='font-size: 16px; color: #555;'>Chào mừng bạn đến với <b>Cửa hàng của chúng tôi!</b> 🎉</p>" +
                            "<p style='font-size: 16px; color: #555;'>Tài khoản của bạn đã được tạo thành công. Chúng tôi rất vui khi có bạn đồng hành!</p>" +
                            "<p style='font-size: 16px; color: #555;'>Nếu có bất kỳ câu hỏi nào hoặc cần hỗ trợ, vui lòng liên hệ với chúng tôi.</p>" +
                            "<div style='text-align: center; margin-top: 20px;'>" +
                            "<a href='https://your-shop.com' style='padding: 10px 20px; background: #007bff; color: white; text-decoration: none; border-radius: 5px;'>Truy cập cửa hàng</a>" +
                            "</div>" +
                            "<p style='text-align: center; font-size: 14px; color: #999; margin-top: 20px;'>Cảm ơn bạn đã tin tưởng chúng tôi! ❤️</p>" +
                            "</div>" +
                            "</body>" +
                            "</html>", username
            );


            emailService.sendEmail(subject, email, content);
            System.out.println("✅ Email đăng ký đã gửi thành công!");
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email: " + e.getMessage());
        }
    });
}

    @PostMapping("/change-password/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    ApiResponse<UserResponse> changePassword(@PathVariable("userId") Integer userId,@RequestBody @Valid PasswordCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result( userService.changePassword(userId,request))
                .build();
    }
    @GetMapping("/{userId}")
    @ApiMessage("Lấy thông tin người dùng theo ID")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    ApiResponse<UserResponse> getUserById(@PathVariable("userId") Integer userId){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .build();
    }


    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ApiResponse<UserResponse> deleteUser(@PathVariable Integer userId){
        return ApiResponse.<UserResponse>builder()
                .result(userService.deleteUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    ApiResponse<UserResponse> updateUser(@PathVariable Integer userId, @RequestPart("user")  UserRequest request, @RequestPart("image") MultipartFile file){
        return ApiResponse.<UserResponse>builder()
                .result(userService.update(userId, request,file))
                .build();
    }
}