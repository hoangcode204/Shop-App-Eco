package com.example.ShopAppEcomere.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderAccount implements Serializable {
    @Id
    private Integer id_product;
    private String name_product;
    private String description;
    private Float price;
    private String img;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Date deleted_at;
    private Integer category_id;
    private Integer id_user;
    private String name_user;
    private Integer status_id;
    private String status;
    private Integer id_order;
}
