package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product>{
    @Query("select o from Product o where o.deletedAt IS NULL")
    List<Product> getAllProduct();

    Page<Product> findAll(Pageable pageable);

    @Query("select o from Product o where o.category.id = :id")
    List<Product> getProductsByCategory(Integer id);

    @Query("SELECT o FROM Product o WHERE o.name_product LIKE CONCAT('%', :name_product, '%') AND o.deletedAt IS NULL")
    List<Product> findProductByName(String name_product);

    @Query("select o from  Product o where  o.id = :id AND o.deletedAt is null ")
    Product findProductById(Integer id);

    @Query("select o from  Product o where  o.price = :price AND o.deletedAt is null ")
    Optional<Product> findProductByPrice(Long price);

    List<Product> findProductByPriceBetween(Integer priceMin, Integer priceMax);


    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR p.name_product LIKE %:name%) AND " +
            "(:category IS NULL OR p.category.name = :category) AND " +
            "(:brand IS NULL OR p.brand = :brand) AND " +
            "(:priceMin IS NULL OR p.price >= :priceMin) AND " +
            "(:priceMax IS NULL OR p.price <= :priceMax) AND " +
            "p.deletedAt IS NULL")
    Page<Product> findFilteredProducts(
            @Param("name") String name,
            @Param("category") String category,
            @Param("brand") String brand,
            @Param("priceMin") Float priceMin,
            @Param("priceMax") Float priceMax,
            Pageable pageable
    );

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR p.name_product LIKE %:name%) AND " +
            "(:category IS NULL OR p.category.name = :category) AND " +
            "(:brand IS NULL OR p.brand = :brand) AND " +
            "(:priceMin IS NULL OR p.price >= :priceMin) AND " +
            "(:priceMax IS NULL OR p.price <= :priceMax) AND " +
            "p.deletedAt IS NULL")
    List<Product> findFilteredProductsNoPage(
            @Param("name") String name,
            @Param("category") String category,
            @Param("brand") String brand,
            @Param("priceMin") Float priceMin,
            @Param("priceMax") Float priceMax
    );

}
