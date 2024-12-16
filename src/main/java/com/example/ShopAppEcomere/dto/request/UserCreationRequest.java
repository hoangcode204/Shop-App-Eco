package com.example.ShopAppEcomere.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserCreationRequest {
    @NotBlank(message = "First Name không được để trống")
    private String firstName;
    @NotBlank(message = "Last Name không được để trống")
    private String lastName;
    @Pattern(regexp = ".*@.*", message = "Email phải đúng định dạng @")
    private String email;
    @Size(min = 6, message = "Mật khẩu tối thiểu 6 kí tự")
    private String password;
    @NotBlank(message = "Adress không được để trống")
    private String address;
   @Size(min = 10, max = 15, message = "Sđt phải đúng định dạng")
    private String phoneNumber;

}
