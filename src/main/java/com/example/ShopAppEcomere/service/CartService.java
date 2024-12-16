package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.CartRequest;
import com.example.ShopAppEcomere.dto.response.cart.CartBasicResponse;
import com.example.ShopAppEcomere.dto.response.cart.CartDetailResponse;
import com.example.ShopAppEcomere.dto.response.orderItem.OrderItemBasicResponse;
import com.example.ShopAppEcomere.dto.response.orderItem.OrderItemDetailResponse;
import com.example.ShopAppEcomere.entity.Cart;
import com.example.ShopAppEcomere.entity.OrderItem;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.CartMapper;
import com.example.ShopAppEcomere.repository.CartRepository;
import com.example.ShopAppEcomere.repository.ProductRepository;
import com.example.ShopAppEcomere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartBasicResponse createCart(CartRequest request) {
        User user=null;
        Product product=null;
        //check userid
        if(request.getUserId()!=null){
             user=userRepository.findById(request.getUserId())
                    .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        }
        //check productId
        if(request.getProductId()!=null){
            product=productRepository.findById(request.getProductId())
                    .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        }

        Cart cart = cartMapper.toCart(request);
        cart.setUser(user);
        cart.setProduct(product);

        return toCartBasicResponse(cartRepository.save(cart));
    }
    @PostAuthorize("returnObject.userId == authentication.token.claims['userId']")
    public CartBasicResponse updateCart(Long cartId, CartRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));

        User user=null;
        Product product=null;
        //check userid
        if(request.getUserId()!=null){
            user=userRepository.findById(request.getUserId())
                    .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        }
        //check productId
        if(request.getProductId()!=null){
            product=productRepository.findById(request.getProductId())
                    .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        }

        cartMapper.updateCart(cart, request);
        cart.setUser(user);
        cart.setProduct(product);
        return toCartBasicResponse(cartRepository.save(cart));
    }
    @PostAuthorize("returnObject.userId == authentication.token.claims['userId']")
    public CartDetailResponse getCartById(Long cartId) {
        return toCartDetailResponse(cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED)));
    }
    @PostAuthorize("returnObject.phoneNumber == authentication.name")
    public void deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));
        cartRepository.delete(cart);
    }


   @PreAuthorize("hasRole('USER')")
    public List<CartDetailResponse> getAllCartsByUser(Long userId) {
        return cartRepository.findAllByUserId(userId).stream()
                .map(this::toCartDetailResponse) // Sử dụng mapper chuyển đổi sang CartDetailResponse
                .collect(Collectors.toList());
    }
    public CartBasicResponse toCartBasicResponse(Cart cart) {
        return CartBasicResponse.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .userId(cart.getUser().getId())
                .productId(cart.getProduct().getId())
                .build();
    }
    public CartDetailResponse toCartDetailResponse(Cart cart) {
        return CartDetailResponse.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .userId(cart.getUser().getId())
                .product(CartDetailResponse.ProductResponse.builder()
                        .id(cart.getProduct() != null ? cart.getProduct().getId() : null)
                        .name(cart.getProduct() != null ? cart.getProduct().getName() : "N/A")
                        .price(cart.getProduct().getPrice())
                        .build())
                .build();
    }

}
