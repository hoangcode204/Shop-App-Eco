package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.dto.request.order.OrderStatusStatisticalDTO;
import com.example.ShopAppEcomere.dto.request.order.OrderTopAccountStatisticalDTO;
import com.example.ShopAppEcomere.dto.request.order.OrderTopProductStatisticalDTO;
import com.example.ShopAppEcomere.dto.request.order.OrderYearStatisticalDTO;
import com.example.ShopAppEcomere.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    @Query("select o from Order o where o.id = :id")
    List<Order> getAllOrderById(Integer id);

    @Query("select o from Order o where o.id = :id")
    Order getOrderById(Integer id);

    @Query("select o from Order o where o.id = :id")
    Optional<Order> findByOrderById(Integer id);

    @Query("select o from Order o where o.user.id = :id")
    List<Order> getOrdersByAccount(Integer id);

    @Query("SELECT o FROM Order o WHERE o.status.id = :statusId")
    List<Order> getAllOrderByStatus(@Param("statusId") Integer statusId);

    @Query("SELECT new com.example.ShopAppEcomere.dto.request.order.OrderStatusStatisticalDTO( " +
            "b.id, b.status, COUNT(a.id)) " +
            "FROM OrderStatus b " +
            "LEFT JOIN Order a ON a.status.id = b.id " +
            "GROUP BY b.id, b.status")
    List<OrderStatusStatisticalDTO> getAllProductByStatus();

    @Query("SELECT new com.example.ShopAppEcomere.dto.request.order.OrderYearStatisticalDTO(MONTH(a.createdAt), SUM(a.totalPrice)) " +
            "FROM Order a WHERE YEAR(a.createdAt) = :year GROUP BY MONTH(a.createdAt)")
    List<OrderYearStatisticalDTO> getAllOrderByYear(@Param("year") String year);

    @Query("SELECT new com.example.ShopAppEcomere.dto.request.order.OrderTopProductStatisticalDTO(a.id, a.name_product, a.price, a.img, a.quantity, a.category, SUM(b.quantity)) " +
            "FROM Product a INNER JOIN OrderItem b ON a.id = b.product.id " +
            "GROUP BY a.id, a.name_product, a.price, a.img, a.quantity, a.category " +
            "ORDER BY COUNT(b.quantity) DESC")
    List<OrderTopProductStatisticalDTO> getTopProduct();
    @Query("SELECT new com.example.ShopAppEcomere.dto.request.order.OrderTopAccountStatisticalDTO(a.id, a.email, a.fullName, a.img, SUM(b.totalPrice)) " +
            "FROM User a INNER JOIN Order b ON a.id = b.user.id " +
            "GROUP BY a.id, a.email, a.fullName, a.img " +
            "ORDER BY SUM(b.totalPrice) DESC")
    List<OrderTopAccountStatisticalDTO> getTopAccount();
    Optional<Order> findByVnpTxnRef(String vnpTxnRef);


}
