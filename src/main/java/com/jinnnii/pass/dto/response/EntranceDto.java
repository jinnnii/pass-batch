package com.jinnnii.pass.dto.response;

import com.jinnnii.pass.domain.constant.ActiveStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record EntranceDto(
        String placeName,
        String packageName,
        Integer price,
        String seatName,
        ActiveStatus status,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        LocalTime remainingTime,
        String qrCode
) {
}
