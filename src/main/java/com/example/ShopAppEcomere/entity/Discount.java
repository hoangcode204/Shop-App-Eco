package com.example.ShopAppEcomere.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discounts")
public class Discount extends  Base  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private  String name;

    private String description;

    private Integer discount_percent;

    private Boolean is_active;
    @JsonIgnore
    @OneToMany(mappedBy = "discount")
    private List<Order> orders;



}
