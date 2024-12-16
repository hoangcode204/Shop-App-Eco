package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.PaymentRequest;
import com.example.ShopAppEcomere.dto.response.PaymentResponse;
import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.entity.Payment;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.PaymentMapper;
import com.example.ShopAppEcomere.repository.PaymentRepository;
import com.example.ShopAppEcomere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;

    public PaymentResponse createPayment(PaymentRequest request) {
        User user=userRepository.findById(request.getUserId())
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        Payment newPayment = paymentMapper.toPayment(request);
        newPayment.setUser(user);
        return paymentMapper.toPaymentResponse(paymentRepository.save(newPayment));
    }
    @PostAuthorize("returnObject.userId == authentication.token.claims['userId']")
    public PaymentResponse getPaymentById(Long id) {
        return paymentMapper.toPaymentResponse(paymentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_EXISTED)));
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deletePayment(Long paymentId) throws AccessDeniedException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_EXISTED));
        // Kiểm tra nếu phone người dùng hiện tại không khớp với phone của user trong payment
        String currentUserPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Authorities: "+currentUserPhone );
        if (!payment.getUser().getPhoneNumber().equals(currentUserPhone)) {
            throw new AccessDeniedException("Bạn không có quyền xóa đơn hàng này");
        }
       payment.setActive(false);
       paymentRepository.save(payment);
    }
    @PostAuthorize("returnObject.userId == authentication.token.claims['userId']")
    public PaymentResponse updatePayment(PaymentRequest request, Long paymentId) {
        User user=userRepository.findById(request.getUserId())
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_EXISTED));
        paymentMapper.updatePayment(payment, request);
        payment.setUser(user);
        return paymentMapper.toPaymentResponse(paymentRepository.save(payment));
    }

    public ResultPaginationDTO fetchAllPayments(Specification<Payment> spec, Pageable pageable) {
        Page<Payment> pagePayments = this.paymentRepository.findAll(spec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pagePayments.getTotalPages());
        meta.setTotal(pagePayments.getTotalElements());

        result.setMeta(meta);

        List<PaymentResponse> paymentList = pagePayments.getContent()
                .stream()
                .map(paymentMapper::toPaymentResponse)
                .collect(Collectors.toList());
        result.setResult(paymentList);

        return result;
    }
}
