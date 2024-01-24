package com.jinnnii.pass.domain.constant;

import lombok.Getter;

public enum PackageType {
    DAY("당일권"),
    TIME("시간권"),
    PERIOD("기간권");

    @Getter
    private final String description;

    PackageType(String description) {
        this.description = description;
    }
}
