package com.jinnnii.pass.domain.constant;

import lombok.Getter;

public enum BulkPassStatus {
    READY("발급전"),
    COMPLETED("발급완료");

    @Getter private final String description;

    BulkPassStatus(String description) {
        this.description = description;
    }
}
