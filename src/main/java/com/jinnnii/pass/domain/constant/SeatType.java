package com.jinnnii.pass.domain.constant;

import lombok.Getter;

public enum SeatType {
    SINGLE("1인실", true),
    CUBE("큐브형", true),
    BUILTIN("붙박이형", true),
    WINDOW("창가형", true),
    OPEN("오픈형", true),
    LAPTOP("노트북형", true),
    PASSAGE("통로", false)
    ;

    @Getter private final String description;
    @Getter private final Boolean isSeat;

    SeatType(String description, Boolean isSeat) {
        this.description = description;
        this.isSeat = isSeat;
    }
}
