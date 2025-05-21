package com.example.ShopAppEcomere.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String email;
    private String username;
    private String fullName;
    private String phoneNumber;
    private Boolean gender;
    private Date date_of_birth;
    private String img;
    private Set<String> roles;
    private Date createdAt;
    private Date updatedAt;
}
