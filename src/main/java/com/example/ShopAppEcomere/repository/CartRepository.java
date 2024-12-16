package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.Cart;
import com.example.ShopAppEcomere.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long>, JpaSpecificationExecutor<Cart> {
    List<Cart> findAllByUserId(Long userId);
}
