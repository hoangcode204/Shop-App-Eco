package com.example.ShopAppEcomere.dto.request;

import com.example.ShopAppEcomere.dto.request.order.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripeCheckoutDTO extends OrderDTO {
    private String successUrl;
//    private String errorUrl;

}

