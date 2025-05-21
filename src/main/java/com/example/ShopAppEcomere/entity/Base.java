package com.example.ShopAppEcomere.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass

public class Base implements Serializable {
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Chỉnh lại MM (tháng) thay vì mm (phút)
    @Column(name = "deleted_at") // Đảm bảo đúng tên trong DB
    private Date deletedAt; // Đổi tên biến về camelCase
    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();

    }
    @PreUpdate
    protected  void onUpdate(){
        updatedAt=LocalDateTime.now();
    }

}
