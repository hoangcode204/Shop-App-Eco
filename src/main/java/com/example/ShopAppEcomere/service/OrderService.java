package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.OrderItem.OrderItemDTO;
import com.example.ShopAppEcomere.dto.request.order.*;
import com.example.ShopAppEcomere.dto.response.order.OrderResponse;
import com.example.ShopAppEcomere.entity.*;
import com.example.ShopAppEcomere.enums.StatusOrder;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.OrderMapper;
import com.example.ShopAppEcomere.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final DiscountRepository discountRepository;
    private final WebSocketService webSocketService;
    private  final NotificationService notificationService;
private final OrderStatusService orderStatusService;

    private final ProductRepository productRepository;

    public List<OrderResponse> getAllOrder(){
       List<Order> orders=orderRepository.findAll();
       return orders.stream().map(orderMapper::toOrderResponse)
               .collect(Collectors.toList());
   }
    public Order getOrderById(Integer id) {
        Order order=orderRepository.findById(id).
                orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_EXISTED));
        return order;
    }
    public OrderResponse setStatusCancel(Integer id) {
        Order order = getOrderById(id);
        OrderStatus orderStatus = orderStatusService.findOrderbyId(StatusOrder.DA_HUY);
        //Tăng lại số lượng sản phẩm
        if(order.getOrderItems()!=null){
            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = productRepository.findProductById(orderItem.getProduct().getId());
                if (product != null) {
                    product.setQuantity(product.getQuantity() + orderItem.getQuantity());
                    productRepository.save(product);
                }
            }
        }
        order.setStatus(orderStatus);
        // Gửi thông báo WebSocket cho admin
        webSocketService.sendCancelledOrderNotification(order.getId(), order.getFullname());
        // Lưu thông báo vào DB cho admin
        String cancelMessage = String.format("Đơn hàng #%d của khách hàng %s đã bị hủy.", order.getId(), order.getFullname());
        notificationService.createNotificationForAdmins(order, cancelMessage);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }
    public List<OrderResponse> getOrdersByAccount(Integer id) {

        List<Order> orders= orderRepository.getOrdersByAccount(id);
        return orders.stream().map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }
    public List<OrderResponse> getAllOrderById(Integer id){
        List<Order> orders= orderRepository.getAllOrderById(id);
        return orders.stream().map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }
    public OrderResponse cancelOrder(Integer id){
        Order order = orderRepository.findByOrderById(id).orElse(null);
        OrderStatus orderStatus = orderStatusService.findOrderbyId(4);
        order.setStatus(orderStatus);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }
    public List<OrderResponse> getAllOrderByStatus(Integer id){
        List<Order> allOrder = orderRepository.getAllOrderByStatus(id);
        allOrder.forEach(item -> {
            item.getOrderItems().forEach(product -> {
                System.out.println(product.getId());
            });
        });
        return allOrder.stream().map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse postOrder(Order order){
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }
    public List<OrderStatusStatisticalDTO> getOrderStatusStatistical() {
        return orderRepository.getAllProductByStatus();
    }
    public List<OrderYearStatisticalDTO> getAllOrderByYear(String year) {
        return orderRepository.getAllOrderByYear(year);
    }
    public List<OrderTopProductStatisticalDTO> getTopProduct() {
        return orderRepository.getTopProduct();
    }
    public List<OrderTopAccountStatisticalDTO> getTopAccount() {
        return orderRepository.getTopAccount();
    }
    @Transactional
    public OrderResponse   createOrder(OrderDTO orderRequestDTO) throws Exception {
        Long totalPrice = 0L;
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setFullname(orderRequestDTO.getFullname());
        order.setPhone(orderRequestDTO.getPhone());
        order.setCity(orderRequestDTO.getCity());
        order.setDistrict(orderRequestDTO.getDistrict());
        order.setWards(orderRequestDTO.getWards());
        order.setSpecific_address(orderRequestDTO.getSpecificAddress());

        // Kiểm tra user có tồn tại không
        User user = userRepository.findByUserId(orderRequestDTO.getUserId());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        order.setUser(user);

        order.setStatus(orderStatusService.findOrderbyId(StatusOrder.CHO_XAC_NHAN));

        List<OrderItem> orderDetails = new ArrayList<>();
        for (OrderItemDTO detailDTO : orderRequestDTO.getOrderDetails()) {
            OrderItem orderDetail = new OrderItem();
            orderDetail.setQuantity(detailDTO.getQuantity());
            orderDetail.setOrder(order);

            // Lấy sản phẩm từ DB
            Product product = productRepository.findProductById(detailDTO.getProductId());
            if (product == null) {
                throw new Exception("Product not found with ID: " + detailDTO.getProductId());
            }

            Integer quantity = detailDTO.getQuantity();
            if (product.getQuantity() < quantity) {
                throw new AppException(ErrorCode.OUT_OF_STOCK);
            }

            Float price = product.getPrice();
            Long total = (long) (quantity * price);

            // Xử lý giá
            orderDetail.setAmount(Float.valueOf(total));
            totalPrice += total;

            // Giảm số lượng sản phẩm
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            orderDetail.setProduct(product);
            orderDetails.add(orderDetail);
        }

        order.setOrderItems(orderDetails);
        order.setTotalPrice(totalPrice);

        // Kiểm tra discount trước khi sử dụng
        if (orderRequestDTO.getDiscountId() != null) {
            Discount discount = discountRepository.findById(orderRequestDTO.getDiscountId()).orElse(null);
            if (discount != null) {
                order.setDiscount(discount);
                order.setTotalPrice(totalPrice * (100 - discount.getDiscount_percent()) / 100);
            }
        }

        // Lưu order vào database
        Order resp = orderRepository.save(order);
        // Gửi thông báo WebSocket cho admin
        webSocketService.sendNewOrderNotification(resp.getId(), resp.getFullname());
        // Lưu thông báo vào DB cho admin
        String newMessage = String.format("Bạn có đơn hàng mới #%d từ khách hàng %s.", resp.getId(), resp.getFullname());
        notificationService.createNotificationForAdmins(resp, newMessage);
        // Gửi email xác nhận
        ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
        emailExecutor.execute(() -> {
            try {
                String email = order.getUser().getEmail();
                String username = order.getUser().getUsername();
                String subject = "Thư cảm ơn ";
                String context = "Chào " + username + ",\n\n" +
                        "Xin chân thành cảm ơn bạn đã mua hàng tại cửa hàng của chúng tôi!...\n";
                emailService.sendEmail(subject, email, context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return orderMapper.toOrderResponse(resp);
    }
    public Order findByTxnRef(String txnRef) {
        return orderRepository.findByVnpTxnRef(txnRef)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với mã giao dịch"));
    }

}
