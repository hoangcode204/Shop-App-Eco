package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.request.PaymentRequest;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.PaymentResponse;
import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.entity.Payment;
import com.example.ShopAppEcomere.service.PaymentService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.persistence.AssociationOverride;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payments")
    @ApiMessage("Create payment")
    public ApiResponse<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.createPayment(request))
                .build();
    }

    @GetMapping("/payments/{paymentId}")
    public ApiResponse<PaymentResponse> getPaymentById(@PathVariable("paymentId") Long paymentId) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.getPaymentById(paymentId))
                .build();
    }

    @GetMapping("/payments")
    public ApiResponse<ResultPaginationDTO> getAllPayments(
            @Filter Specification<Payment> spec,
            Pageable pageable) {
        return ApiResponse.<ResultPaginationDTO>builder()
                .result(paymentService.fetchAllPayments(spec, pageable))
                .build();
    }

    @PutMapping("/payments/{paymentId}")
    public ApiResponse<PaymentResponse> updatePayment(
            @PathVariable Long paymentId,
            @RequestBody PaymentRequest request) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.updatePayment(request, paymentId))
                .build();
    }

    @DeleteMapping("/payments/{paymentId}")
    public ApiResponse<Void> deletePayment(@PathVariable("paymentId") Long paymentId) throws AccessDeniedException {
        paymentService.deletePayment(paymentId);
        return ApiResponse.<Void>builder()
                .message("Delete payment successful")
                .build();
    }
}
