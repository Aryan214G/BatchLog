package com.log.ui.util;

public final class StringUtils {

    private StringUtils() {}

    public static String nullIfBlank(String text) {
        return (text == null || text.isBlank()) ? null : text;
    }
}
