package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.OrderAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderAccountRepository extends JpaRepository<OrderAccount,Integer> {
    @Query("SELECT NEW com.example.ShopAppEcomere.entity.OrderAccount(pr.id,pr.name_product,pr.description,pr.price,pr.img,od.quantity,pr.createdAt,pr.updatedAt,pr.deletedAt,pr.category.id,a.id,a.username,s.id,s.status,o.id) " +
            "FROM Order o " +
            "LEFT JOIN User a ON a.id = o.user.id " +
            "LEFT JOIN OrderItem od ON od.order.id = o.id " +
            "LEFT JOIN Product pr ON pr.id = od.product.id " +
            "LEFT JOIN OrderStatus s ON s.id = o.status.id " +
            "LEFT JOIN Category ca ON ca.id = pr.category.id " +
            "WHERE a.id= :idacc")
    List<OrderAccount> getOrderByAcc(@Param("idacc") Integer idacc);

    @Query("SELECT NEW com.example.ShopAppEcomere.entity.OrderAccount(pr.id,pr.name_product,pr.description,pr.price,pr.img,od.quantity,pr.createdAt,pr.updatedAt,pr.deletedAt,pr.category.id,a.id,a.username,s.id,s.status,o.id) " +
            "FROM Order o " +
            "LEFT JOIN User a ON a.id = o.user.id " +
            "LEFT JOIN OrderItem od ON od.order.id = o.id " +
            "LEFT JOIN Product pr ON pr.id = od.product.id " +
            "LEFT JOIN OrderStatus s ON s.id = o.status.id " +
            "LEFT JOIN Category ca ON ca.id = pr.category.id " +
            "WHERE a.id= :idacc AND s.id= :status")
    List<OrderAccount> getOrderByAccAndStatus(@Param("idacc") Integer idacc, @Param("status") Integer status);
}
