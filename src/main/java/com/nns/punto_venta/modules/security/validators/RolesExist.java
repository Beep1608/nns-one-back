package com.nns.punto_venta.modules.security.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nns.punto_venta.modules.security.validators.RolesExistValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = RolesExistValidator.class) // <--- Verifica que apunte a la clase correcta
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RolesExist {
    String message() default "El rol especificado no existe o no es permitido";
    Class<?>[] groups() default {};
    Class<? extends  Payload>[] payload() default {};
}

