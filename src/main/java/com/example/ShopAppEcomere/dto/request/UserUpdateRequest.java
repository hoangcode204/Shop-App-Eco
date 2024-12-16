package com.example.ShopAppEcomere.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String address;
    private String email;
    List<String> roles;
}
