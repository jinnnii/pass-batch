package com.jinnnii.pass.domain.constant;

import lombok.Getter;

public enum RoleType {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    DEVELOPER("ROLE_DEVELOPER");

    @Getter private final String roleName;

    RoleType(String roleName) {
        this.roleName = roleName;
    }
}
