package com.nns.punto_venta.modules.security.entities;

public enum RoleCatalog {
    ADMIN("Admin"),
    USER("User");

    private final String name;

    RoleCatalog(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean isValid(String roleName) {
        for (RoleCatalog role : RoleCatalog.values()) {
            if (role.getName().equalsIgnoreCase(roleName)) {
                return true;
            }
        }
        return false;
    }
}
