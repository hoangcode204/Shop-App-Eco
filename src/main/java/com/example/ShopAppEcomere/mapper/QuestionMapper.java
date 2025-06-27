package com.example.ShopAppEcomere.mapper;

import com.example.ShopAppEcomere.dto.request.QuestionRequest;
import com.example.ShopAppEcomere.dto.response.question.QuestionResponse;
import com.example.ShopAppEcomere.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface QuestionMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "admin", ignore = true)
    Question toQuestion(QuestionRequest request);

    QuestionResponse toQuestionResponse(Question question);

    void updateQuestion(@MappingTarget Question question, QuestionRequest request);
}