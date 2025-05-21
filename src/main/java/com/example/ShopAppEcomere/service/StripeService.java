package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.OrderItem.OrderItemDTO;
import com.example.ShopAppEcomere.dto.request.StripeCheckoutDTO;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.repository.DiscountRepository;
import com.example.ShopAppEcomere.repository.ProductRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StripeService {
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final DiscountRepository discountRepository;

    static {
        Stripe.apiKey = "sk_test_51R8bNYEKZGIKs93KccZfFDYdtNCJbqXwSrpKuiDqYdI0iVFkVJJtaxSUjgiKTTvRQHSBJSST6Nz5TRaOj9g6GhaV00rlUgEws9";
    }

    public String checkout(StripeCheckoutDTO stripeCheckoutDTO) throws StripeException {
        if (stripeCheckoutDTO == null) {
            throw new IllegalArgumentException("StripeCheckoutDTO cannot be null");
        }

        // Kiểm tra orderDetails có null không
        if (stripeCheckoutDTO.getOrderDetails() == null || stripeCheckoutDTO.getOrderDetails().isEmpty()) {
            throw new IllegalArgumentException("Order details cannot be null or empty");
        }

        try {
            orderService.createOrder(stripeCheckoutDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating order", e);
        }

        SessionCreateParams.Builder builder = new SessionCreateParams.Builder();
        builder.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD);
        builder.setMode(SessionCreateParams.Mode.PAYMENT);
        builder.setSuccessUrl(stripeCheckoutDTO.getSuccessUrl());
        builder.setCancelUrl(stripeCheckoutDTO.getErrorUrl());

        for (OrderItemDTO orderDetail : stripeCheckoutDTO.getOrderDetails()) {
            if (orderDetail == null || orderDetail.getProductId() == null) {
                continue; // Bỏ qua item nếu null
            }

            Product product = productRepository.findById(orderDetail.getProductId()).orElse(null);
            if (product != null) {
                builder.addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity((long) orderDetail.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(product.getPrice() != null ? product.getPrice().longValue() : 0)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(product.getName_product())
                                                                .build())
                                                .build())
                                .build());
            }
        }

        SessionCreateParams params = builder.build();

        try {
            Session session = Session.create(params);
            return session.getUrl();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating Stripe session", e);
        }
    }
}
