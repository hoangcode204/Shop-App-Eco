//package com.example.ShopAppEcomere.validator;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//
//import java.util.Arrays;
//
//public class ShipmentStatusSubsetValidator implements ConstraintValidator<ShipmentStatusSubset, ShipmentStatusEnum> {
//    private ShipmentStatusEnum[] allowedStatuses;
//
//    @Override
//    public void initialize(ShipmentStatusSubset constraint) {
//        this.allowedStatuses = constraint.anyOf();
//    }
//
//    @Override
//    public boolean isValid(ShipmentStatusEnum value, ConstraintValidatorContext context) {
//        return value == null || Arrays.asList(allowedStatuses).contains(value);
//    }
//}
