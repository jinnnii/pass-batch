package com.jinnnii.pass.domain.constant;

import lombok.Getter;

public enum SeatType {
    SINGLE("1인실"),
    CUBE("큐브형"),
    BUILTIN("붙박이형"),
    WINDOW("창가형"),
    OPEN("오픈형"),
    LAPTOP("노트북형"),
    PASSAGE("통로")
    ;

    @Getter
    private final String description;

    SeatType(String description) {
        this.description = description;
    }
}
