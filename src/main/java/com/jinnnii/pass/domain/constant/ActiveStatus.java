package com.jinnnii.pass.domain.constant;

import lombok.Getter;

public enum ActiveStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화");

    @Getter private final String description;

    ActiveStatus(String description) {
        this.description = description;
    }
}
