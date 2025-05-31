package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String> {
    Optional<ResetPasswordToken> findByToken(String token);

}
