package com.example.ShopAppEcomere.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String email;
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private Boolean gender;
    private Date date_of_birth;
    private String img;
}