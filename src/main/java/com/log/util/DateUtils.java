package com.log.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {


    public String getCurrentDateFormatted(){

        LocalDate currentDate = LocalDate.now();

        // Define your desired pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Format the current date
        String formattedDate = currentDate.format(formatter);

        return formattedDate;
    }

}
