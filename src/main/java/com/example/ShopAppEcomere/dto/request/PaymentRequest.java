package com.example.ShopAppEcomere.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class PaymentRequest {

    @NotNull(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Amount is required")
    private int amount;

    private Date paymentDate;
    @NotNull(message = "userId is required")
    private Long userId;
}
