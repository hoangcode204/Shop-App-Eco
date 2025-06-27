package com.example.ShopAppEcomere.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name_product;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Float price;
    private String img;
    private Integer quantity;
    private String brand;
    private Float promotionalPrice;
    private String descriptionShort;
    private Long totalSold = 0L;
    // Quan hệ với Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore // Tránh vòng lặp khi serialize JSON
    private Category category;

    // Quan hệ với OrderItem
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // Tránh vòng lặp khi serialize JSON
    private List<OrderItem> orderItems;

    // Quan hệ với Gallery
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Gallery> galleries;

    // Quan hệ với Review
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviews;


}
