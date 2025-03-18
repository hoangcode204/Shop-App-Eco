package com.example.ShopAppEcomere.validator;

import com.example.ShopAppEcomere.enums.OrderStatusEnum;
import com.example.ShopAppEcomere.enums.ShipmentStatusEnum;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validator cho OrderStatusEnum, chỉ cho phép các giá trị trong danh sách được chỉ định.
 */
@Documented
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ShipmentStatusSubsetValidator.class)
public @interface ShipmentStatusSubset {
    ShipmentStatusEnum[] anyOf();
    String message() default "Shipment status must be one of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
