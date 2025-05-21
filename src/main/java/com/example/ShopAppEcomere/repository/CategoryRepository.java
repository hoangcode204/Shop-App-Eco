package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.dto.response.category.CategoryResponse;
import com.example.ShopAppEcomere.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>, JpaSpecificationExecutor<Category> {
boolean existsByName(String categoryName);
    @Query("select o from Category o where o.deletedAt is null ")
    List<Category> getAllCategory();

    @Query("select o from Category o where o.id = :id and o.deletedAt is null ")
    Optional<Category> getCategoryById(Integer id);

}
