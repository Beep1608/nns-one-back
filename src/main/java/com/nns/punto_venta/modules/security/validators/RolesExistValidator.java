package com.nns.punto_venta.modules.security.validators;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nns.punto_venta.modules.security.repositories.RoleRepository;
import com.nns.punto_venta.modules.security.validators.RolesExist;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
@Component
// 1. Cambiamos Long[] por List<Long> en la implementación
public class RolesExistValidator implements ConstraintValidator<RolesExist, List<Long>> {

    private final RoleRepository roleRepository;

    public RolesExistValidator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    // 2. Cambiamos el parámetro de Long[] a List<Long>
    public boolean isValid(List<Long> roles, ConstraintValidatorContext context) {
        // 3. Ahora 'roles' ya es una lista, no necesitas Arrays.asList()
        if (roles == null || roles.isEmpty()) {
            return true;
        }

        // 4. Usamos la lista directamente
        long count = roleRepository.countByIdIn(roles);
        
        return count == roles.size();
    }
}

