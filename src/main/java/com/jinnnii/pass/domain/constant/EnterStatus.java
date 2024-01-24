package com.jinnnii.pass.domain.constant;

import lombok.Getter;

public enum EnterStatus {
    READY("준비"),
    ENTERED("입장"),
    EXPIRED("만료");

    @Getter private final String description;

    EnterStatus(String description) {
        this.description = description;
    }
}
