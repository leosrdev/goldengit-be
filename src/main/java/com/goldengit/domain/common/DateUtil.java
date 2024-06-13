package com.goldengit.domain.common;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    public static final String DATE_FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static float calculateCycleTime(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_UTC);
        LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);
        long hours = ChronoUnit.HOURS.between(startDateTime, endDateTime);
        float days = (float) hours / 24;
        DecimalFormat df = new DecimalFormat("#.#");
        return Float.parseFloat(df.format(days));
    }
}
