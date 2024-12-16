package com.example.ShopAppEcomere.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@MappedSuperclass
public class Base {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean active = true;
    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();

    }
    @PreUpdate
    protected  void onUpdate(){
        updatedAt=LocalDateTime.now();
    }
}
