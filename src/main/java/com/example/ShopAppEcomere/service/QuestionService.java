package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.AnswerRequest;
import com.example.ShopAppEcomere.dto.request.QuestionRequest;
import com.example.ShopAppEcomere.dto.response.NotificationDTO;
import com.example.ShopAppEcomere.dto.response.question.QuestionResponse;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.entity.Question;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.enums.QuestionStatus;
import com.example.ShopAppEcomere.enums.RoleEnum;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.QuestionMapper;
import com.example.ShopAppEcomere.repository.ProductRepository;
import com.example.ShopAppEcomere.repository.QuestionRepository;
import com.example.ShopAppEcomere.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class QuestionService {
    QuestionRepository questionRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    QuestionMapper questionMapper;
    WebSocketService webSocketService;


    public QuestionResponse createQuestion(QuestionRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        Question question = questionMapper.toQuestion(request);
        question.setUser(user);
        question.setProduct(product);

        Question savedQuestion = questionRepository.save(question);

        // Notify admin via WebSocket
        NotificationDTO notification = new NotificationDTO(
                "NEW_QUESTION",
                "Có câu hỏi mới cho sản phẩm: " + product.getName_product(),
                savedQuestion.getId(),
                user.getFullName(),
                LocalDateTime.now(),
                "Chờ trả lời"
        );
        webSocketService.sendNotificationToAdmin(notification);

        return questionMapper.toQuestionResponse(savedQuestion);
    }

    public QuestionResponse answerQuestion(Integer questionId, AnswerRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Optional: Check if the user has an ADMIN role
        boolean isAdmin = admin.getRoles().stream()
                .anyMatch(role -> role.getName().equals(RoleEnum.ADMIN.name()));
        if(!isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

        question.setAnswerText(request.getAnswerText());
        question.setAdmin(admin);
        question.setStatus(QuestionStatus.ANSWERED);

        Question updatedQuestion = questionRepository.save(question);
        return questionMapper.toQuestionResponse(updatedQuestion);
    }

    public List<QuestionResponse> getQuestionsByProductId(Integer productId) {
        List<Question> questions = questionRepository.findByProduct_Id(productId);
        return questions.stream()
                .map(questionMapper::toQuestionResponse)
                .collect(Collectors.toList());
    }

    public List<QuestionResponse> getUnansweredQuestions() {
        List<Question> questions = questionRepository.findByStatus(QuestionStatus.PENDING);
        return questions.stream()
                .map(questionMapper::toQuestionResponse)
                .collect(Collectors.toList());
    }

    public void deleteQuestion(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        questionRepository.delete(question);
    }
}