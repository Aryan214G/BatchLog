package com.log.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DateUtils() {
    }

    public static String getCurrentDateFormatted() {
        return format(LocalDate.now());
    }

    public static String format(LocalDate date) {

        if (date == null) {
            return "";
        }

        return date.format(FORMATTER);
    }

    public static String format(String isoDate) {

    if (isoDate == null || isoDate.isBlank()) {
        return "";
    }

    return LocalDate.parse(isoDate)
            .format(FORMATTER);
}
}