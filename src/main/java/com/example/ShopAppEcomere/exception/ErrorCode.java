package com.example.ShopAppEcomere.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(1002, "Category existed", HttpStatus.BAD_REQUEST),
    ORDER_EXISTED(1002, "Order existed", HttpStatus.BAD_REQUEST),
    ORDER_ITEM_EXISTED(1002, "OrderItem existed", HttpStatus.BAD_REQUEST),
    PRODUCT_EXISTED(1002, "Product existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_EXISTED(1005, "Product not existed", HttpStatus.NOT_FOUND),
    ORDER_NOT_EXISTED(1005, "Order not existed", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_EXISTED(1005, "Category not existed", HttpStatus.NOT_FOUND),
    CART_NOT_EXISTED(1005, "Cart not existed", HttpStatus.NOT_FOUND),
    ORDER_ITEM_NOT_EXISTED(1005, "Order_Item not existed", HttpStatus.NOT_FOUND),
    PAYMENT_NOT_EXISTED(1005, "Payment not existed", HttpStatus.NOT_FOUND),
    SHIPMENT_NOT_EXISTED(1005, "Shipment not existed", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    PASSWORD_EXISTED(1009, "Password is exited", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
