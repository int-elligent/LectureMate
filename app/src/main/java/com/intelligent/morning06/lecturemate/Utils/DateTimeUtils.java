package com.intelligent.morning06.lecturemate.Utils;


import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    public static String FormatDateTimeToMonthAndYear(LocalDateTime dateTime) {
        Date date = Date.from(dateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat dateFormat = new SimpleDateFormat( "LLLL yyyy", Locale.getDefault() );
        return dateFormat.format(date);
    }

    public static String FormatDateTimeAsNormalDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return formatter.format(dateTime);
    }
}
