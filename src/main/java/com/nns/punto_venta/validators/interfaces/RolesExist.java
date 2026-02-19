package com.nns.punto_venta.validators.interfaces;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nns.punto_venta.validators.implementations.RolesExistValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = RolesExistValidator.class) // <--- Verifica que apunte a la clase correcta
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RolesExist {
    String message() default "Uno o más IDs de roles no existen en la base de datos";
    Class<?>[] groups() default {};
    Class<? extends  Payload>[] payload() default {};
}
