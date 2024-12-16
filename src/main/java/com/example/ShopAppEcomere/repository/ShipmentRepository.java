package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.Category;
import com.example.ShopAppEcomere.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Long>, JpaSpecificationExecutor<Shipment> {

}
