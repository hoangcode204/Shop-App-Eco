package com.example.ShopAppEcomere.dto.response;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

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

}
