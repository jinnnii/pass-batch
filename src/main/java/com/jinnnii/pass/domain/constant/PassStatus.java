package com.jinnnii.pass.domain.constant;

import lombok.Getter;

public enum PassStatus {
    READY("준비"),
    PROGRESSED("진행"),
    EXPIRED("만료");

    @Getter
    private final String description;

    PassStatus(String description) {
        this.description = description;
    }
}
