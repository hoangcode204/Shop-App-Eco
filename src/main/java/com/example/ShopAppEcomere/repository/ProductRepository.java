package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
boolean existsByName(String name);
}
