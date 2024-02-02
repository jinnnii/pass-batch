package com.jinnnii.pass.util;

import io.micrometer.common.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtils {
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");

    public static String format(final LocalDateTime localDateTime){
        return localDateTime.format(YYYY_MM_DD_HH_MM);
    }

    public static String format(final LocalTime localTime){
        return localTime.format(HH_MM);
    }

    public static LocalDateTime parse(final String localDateTimeStr){
        if (StringUtils.isBlank(localDateTimeStr)) return null;
        return LocalDateTime.parse(localDateTimeStr, YYYY_MM_DD_HH_MM);
    }
}
