package com.example.ShopAppEcomere.repository;

import com.example.ShopAppEcomere.entity.Question;
import com.example.ShopAppEcomere.enums.QuestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByProduct_Id(Integer productId);
    List<Question> findByStatus(QuestionStatus status);
}