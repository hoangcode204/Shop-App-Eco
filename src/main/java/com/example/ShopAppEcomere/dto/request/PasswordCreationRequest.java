package com.example.ShopAppEcomere.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordCreationRequest {
    @Size(min = 3, message = "INVALID_PASSWORD")
    String oldPassword;
    @Size(min = 6, message = "INVALID_PASSWORD")
    String newPassword;
}

