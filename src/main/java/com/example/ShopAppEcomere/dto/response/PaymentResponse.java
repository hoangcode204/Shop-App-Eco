package com.example.ShopAppEcomere.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class PaymentResponse {

    private Long id;
    private String paymentMethod;
    private int amount;
    private Date paymentDate;
    private Long userId;
    private Boolean active;

}
