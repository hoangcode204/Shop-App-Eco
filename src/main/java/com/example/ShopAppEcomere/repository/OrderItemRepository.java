package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.id = :productId AND oi.order.status.id = :statusId")
    Long sumQuantityByProductIdAndOrderStatus(@Param("productId") Integer productId, @Param("statusId") Integer statusId);
}
