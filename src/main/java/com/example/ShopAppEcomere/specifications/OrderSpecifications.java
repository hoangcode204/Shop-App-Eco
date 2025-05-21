//package com.example.ShopAppEcomere.specifications;
//
//import com.example.ShopAppEcomere.entity.Order;
//import org.springframework.data.jpa.domain.Specification;
//
//public class OrderSpecifications {
//    // Lọc đơn hàng theo userId
//    public static Specification<Order> hasUserId(Long userId) {
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.equal(root.get("user").get("id"), userId);
//    }
//
//    // Lọc đơn hàng theo trạng thái
//    public static Specification<Order> hasStatus(String status) {
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.equal(root.get("status"), status);
//    }
//}
