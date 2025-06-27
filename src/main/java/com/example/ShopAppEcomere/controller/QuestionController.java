package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.AnswerRequest;
import com.example.ShopAppEcomere.dto.request.QuestionRequest;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.question.QuestionResponse;
import com.example.ShopAppEcomere.service.QuestionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionController {

    QuestionService questionService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ApiResponse<QuestionResponse> createQuestion(@RequestBody @Valid QuestionRequest request) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.createQuestion(request))
                .build();
    }

    @GetMapping("/product/{productId}")
    public ApiResponse<List<QuestionResponse>> getQuestionsByProduct(@PathVariable Integer productId) {
        return ApiResponse.<List<QuestionResponse>>builder()
                .result(questionService.getQuestionsByProductId(productId))
                .build();
    }

    @PostMapping("/{questionId}/answer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<QuestionResponse> answerQuestion(@PathVariable Integer questionId, @RequestBody @Valid AnswerRequest request) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.answerQuestion(questionId, request))
                .build();
    }

    @GetMapping("/unanswered")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<List<QuestionResponse>> getUnansweredQuestions() {
        return ApiResponse.<List<QuestionResponse>>builder()
                .result(questionService.getUnansweredQuestions())
                .build();
    }

    @DeleteMapping("/{questionId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<Void> deleteQuestion(@PathVariable Integer questionId) {
        questionService.deleteQuestion(questionId);
        return ApiResponse.<Void>builder().build();
    }
}