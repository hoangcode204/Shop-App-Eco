package com.example.ShopAppEcomere.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
public class UserResponse {
    private long id;
    String email;
    String username;
    String fullName;
    String phoneNumber;
    private Boolean gender;
    private Date date_of_birth;
    private String img;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Date delete_at;
    private Set<RoleResponse> role;  // Thay vì một RoleResponse, bạn có thể sử dụng Set

}
