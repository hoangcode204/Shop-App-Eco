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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date delete_at;
    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();

    }
    @PreUpdate
    protected  void onUpdate(){
        updatedAt=LocalDateTime.now();
    }

}
