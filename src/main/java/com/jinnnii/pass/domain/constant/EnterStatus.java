package com.jinnnii.pass.domain.constant;

import lombok.Getter;

public enum EnterStatus {
    READY("준비", false),
    ENTERED("입장", true),
    EXPIRED("만료", false);

    @Getter private final String description;
    @Getter private final Boolean isEnter;

    EnterStatus(String description, Boolean isEnter) {
        this.description = description;
        this.isEnter = isEnter;
    }
}
