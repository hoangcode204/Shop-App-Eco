package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Integer> {

    @Query("select o from Gallery o where o.product.id = :id")
    List<Gallery> getGalleriesByProductId(Integer id);
}