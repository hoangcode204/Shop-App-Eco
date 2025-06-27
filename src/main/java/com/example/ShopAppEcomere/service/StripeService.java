package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.config.VNPayConfig;
import com.example.ShopAppEcomere.dto.request.OrderItem.OrderItemDTO;
import com.example.ShopAppEcomere.dto.request.StripeCheckoutDTO;
import com.example.ShopAppEcomere.dto.response.order.OrderResponse;
import com.example.ShopAppEcomere.entity.Order;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.repository.DiscountRepository;
import com.example.ShopAppEcomere.repository.OrderRepository;
import com.example.ShopAppEcomere.repository.ProductRepository;
import com.example.ShopAppEcomere.util.VNPayUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class StripeService {
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final DiscountRepository discountRepository;
    private final VNPayConfig vnpayConfig; // ✅ THÊM DÒNG NÀY
    private final OrderRepository orderRepository;
    public String checkout(StripeCheckoutDTO checkoutDTO) {
        if (checkoutDTO == null) {
            throw new IllegalArgumentException("CheckoutDTO cannot be null");
        }

        if (checkoutDTO.getOrderDetails() == null || checkoutDTO.getOrderDetails().isEmpty()) {
            throw new IllegalArgumentException("Order details cannot be null or empty");
        }

        try {
            // B1: Tạo đơn hàng
            OrderResponse orderResponse = orderService.createOrder(checkoutDTO);

            // Lấy lại entity Order từ database để đảm bảo có totalPrice đã áp dụng discount
            Order order = orderService.getOrderById(orderResponse.getId());
            if (order == null) {
                throw new RuntimeException("Order not found after creation.");
            }
            long totalAmount = order.getTotalPrice(); // Sử dụng totalPrice đã có discount từ DB

            // B4: Tạo mã giao dịch duy nhất
            String txnRef = "ORDER" + System.currentTimeMillis();

            // B5: Lưu mã giao dịch và đánh dấu chưa thanh toán (CẦN cập nhật lại trong DB)
            order.setVnpTxnRef(txnRef);
            order.setIsPaid(false);
            orderRepository.save(order); // Cập nhật lại

            // B6: Tạo URL thanh toán VNPay
            String ipAddr = "127.0.0.1"; // Nên lấy từ HttpServletRequest nếu có
            System.out.println("VNPay final totalAmount sent: " + totalAmount);
            String paymentUrl = VNPayUtil.createVNPayUrl(txnRef, totalAmount, ipAddr, vnpayConfig);

            return paymentUrl;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing VNPay checkout", e);
        }
    }


}
