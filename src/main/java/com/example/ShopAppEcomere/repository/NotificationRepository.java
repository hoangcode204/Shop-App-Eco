package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.Notification;
import com.example.ShopAppEcomere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientAndIsReadFalseOrderByCreatedAtDesc(User recipient);
}