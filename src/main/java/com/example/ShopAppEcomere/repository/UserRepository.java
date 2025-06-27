package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select o from User o where o.email = :email and o.password = :password AND o.deletedAt IS NULL")
   User findUserByEmailAndPassword(String email, String password);

    @Query("select o from User o where o.email = :email AND o.deletedAt IS NULL")
    Optional<User> findByEmail(String email);  //find account by email

    @Query("select o from User o where  o.id = :id AND o.deletedAt is null ")
    User findByUserId(Integer id);
    boolean existsByPhoneNumberAndDeletedAtIsNull(String phoneNumber);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);


}
