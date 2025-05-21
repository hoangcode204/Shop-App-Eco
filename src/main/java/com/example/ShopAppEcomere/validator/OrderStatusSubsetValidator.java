//package com.example.ShopAppEcomere.validator;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//
//import java.util.Arrays;
//
//public class OrderStatusSubsetValidator implements ConstraintValidator<OrderStatusSubset, OrderStatusEnum> {
//    private OrderStatusEnum[] allowedStatuses;
//
//    @Override
//    public void initialize(OrderStatusSubset constraint) {
//        this.allowedStatuses = constraint.anyOf();
//    }
//
//    @Override
//    public boolean isValid(OrderStatusEnum value, ConstraintValidatorContext context) {
//        return value == null || Arrays.asList(allowedStatuses).contains(value);
//    }
//}
