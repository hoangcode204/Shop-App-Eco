package com.example.ShopAppEcomere.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "orders")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long totalPrice;
    ////Add information
    private String fullname;

    private String phone;

    private String city;

    private String district;

    private String wards;

    private String specific_address;
    //
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private OrderStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "discount_id")
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private Discount discount;
}
