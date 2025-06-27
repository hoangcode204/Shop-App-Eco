package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.config.VNPayConfig;
import com.example.ShopAppEcomere.dto.request.StripeCheckoutDTO;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.entity.Order;
import com.example.ShopAppEcomere.entity.OrderStatus;
import com.example.ShopAppEcomere.enums.StatusOrder;
import com.example.ShopAppEcomere.repository.OrderRepository;
import com.example.ShopAppEcomere.service.OrderService;
import com.example.ShopAppEcomere.service.OrderStatusService;
import com.example.ShopAppEcomere.service.StripeService;
import com.example.ShopAppEcomere.util.VNPayUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/stripe")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;
    private final OrderService orderService;
    private final VNPayConfig vnpayConfig;
    private final OrderRepository orderRepository;
    private final OrderStatusService orderStatusService;

    /**
     * API khởi tạo thanh toán VNPay
     */
    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ApiResponse<?> checkout(@RequestBody StripeCheckoutDTO stripeCheckoutDTO) {
        String paymentUrl = stripeService.checkout(stripeCheckoutDTO);
        return ApiResponse.builder()
                .result(paymentUrl)
                .build();
    }

    /**
     * API xử lý khi VNPay redirect về
     */
    @GetMapping("/vnpay-return")
    public void handleVNPayReturn(@RequestParam Map<String, String> params,
                                  HttpServletResponse response) throws IOException {
        System.out.println("VNPay Callback Parameters: " + params);
        String vnpTxnRef = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");          // Mã phản hồi
        String transactionStatus = params.get("vnp_TransactionStatus"); // Trạng thái GD

        // Xác thực chữ ký
        boolean isValid = VNPayUtil.verifySignature(params, vnpayConfig.getHashSecret());
        System.out.println("VNPay Signature Valid: " + isValid);

        if (!isValid) {
            System.out.println("VNPay: Signature is NOT valid. Redirecting to cancel.");
            response.sendRedirect("http://localhost:8086/cancel"); // ❌ Chữ ký không hợp lệ
            return;
        }

        Order order = orderService.findByTxnRef(vnpTxnRef);
        System.out.println("VNPay: Order found with TxnRef " + vnpTxnRef + ": " + (order != null));
        if (order == null) {
            System.out.println("VNPay: Order NOT found. Redirecting to cancel.");
            response.sendRedirect("http://localhost:8086/cancel"); // ❌ Không tìm thấy đơn hàng
            return;
        }

        // 🎯 Phân biệt thành công và thất bại
        if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
            System.out.println("VNPay: Payment successful. Updating order status and redirecting to success.");
            order.setIsPaid(true);
            // Set order status to "In Transit" (DANG_GIAO)
            OrderStatus inTransitStatus = orderStatusService.findOrderbyId(StatusOrder.DANG_GIAO);
            order.setStatus(inTransitStatus);
            orderRepository.save(order); // Cập nhật trạng thái thanh toán và vận chuyển
            response.sendRedirect("http://localhost:8086/success"); // ✅ Giao dịch thành công
        } else {
            System.out.println("VNPay: Payment failed or cancelled. Updating order status and redirecting to cancel.");
            order.setIsPaid(false);
            orderRepository.save(order); // Cập nhật trạng thái thanh toán
            response.sendRedirect("http://localhost:8086/cancel"); // ❌ Giao dịch bị hủy/lỗi
        }
    }

}
