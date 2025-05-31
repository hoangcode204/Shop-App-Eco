package com.example.ShopAppEcomere.dto.response.auth;

import com.example.ShopAppEcomere.dto.response.UserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String token;
    boolean authenticated;
    UserResponse user;
}
