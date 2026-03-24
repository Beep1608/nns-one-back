package com.nns.punto_venta.modules.security.validators;

import org.springframework.stereotype.Component;

import com.nns.punto_venta.modules.security.repositories.RoleRepository;
import com.nns.punto_venta.modules.security.validators.RolesExist;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.nns.punto_venta.modules.security.entities.RoleCatalog;

@Component
public class RolesExistValidator implements ConstraintValidator<RolesExist, String> {

    private final RoleRepository roleRepository;

    public RolesExistValidator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean isValid(String roleName, ConstraintValidatorContext context) {
        if (roleName == null || roleName.isEmpty()) {
            return true;
        }

        // 1. Validar contra el Catálogo (Enum)
        if (!RoleCatalog.isValid(roleName)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El rol '" + roleName + "' no es permitido. Solo se aceptan: Admin, User")
                   .addConstraintViolation();
            return false;
        }

        // 2. Validar que existe en la Base de Datos
        return roleRepository.findByName(roleName).isPresent();
    }
}

