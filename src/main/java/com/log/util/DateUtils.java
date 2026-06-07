package com.log.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {


     private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DateUtils() {
    }

    public static String getCurrentDateFormatted() {
        return LocalDate.now().format(FORMATTER);
    }

}
