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
    ADDRESS_NOT_EXISTED(1010,"Address is not exited",HttpStatus.NOT_FOUND),
    CANNOT_DELETE_DEFAULT_ADDRESS(1011,"Can not delete default address",HttpStatus.BAD_REQUEST),
    ADDRESS_DEFAULT_NOT_FOUND(1012,"Address_default is not exited",HttpStatus.NOT_FOUND),
    ADDRESS_ALREADY_DEFAULT(1013,"Address is exited",HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(1014,"Category not found",HttpStatus.NOT_FOUND),
    PASSWORD_INCORRECT(1015,"Password incorrect",HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS(1016,"INVALID_ORDER_STATUS",HttpStatus.BAD_REQUEST),
    OUT_OF_STOCK(1017,"Out of stock",HttpStatus.BAD_REQUEST),
    GALLERY_NOT_EXISTED(1018,"Gallery not existed",HttpStatus.NOT_FOUND),
    INVALID_RESET_TOKEN(1019,"Invalid reset token",HttpStatus.BAD_REQUEST),
    RESET_TOKEN_EXPIRED(1020,"Reset token expired",HttpStatus.BAD_REQUEST),
    REVIEW_NOT_EXISTED(1021,"Review not existed",HttpStatus.NOT_FOUND),
    NOTIFICATION_NOT_FOUND(1022,"Notification not found",HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND(1023, "Question not found", HttpStatus.NOT_FOUND),
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
