package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {
    @Query("select o from Address o where o.user.deletedAt is null")
    List<Address> getAllAddress();

    @Query("select o from Address o where o.user.id = :id and o.user.deletedAt is null")
    List<Address> findAddressByUserId(Integer id);

    @Query("select o from Address o where o.is_default = TRUE and o.user.id = :id")
    Address findAddressDefaultByUserId(Integer id);
}
