package com.intelligent.morning06.lecturemate.Utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class DateTimeUtils {

    public static String FormatDateTimeToMonthAndYear(LocalDateTime dateTime) {
        Date date = Date.from(dateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat dateFormat = new SimpleDateFormat( "LLLL yyyy", Locale.ENGLISH );
        return dateFormat.format(date);
    }

    public static String FormatDateTimeAsNormalDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(dateTime);
    }

    public static String FormatDateTimeAsNormalTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("kk:mm");
        return formatter.format(dateTime);
    }

    public static String getRelativeDate(String reference_date) {
        SimpleDateFormat relativeDF = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date reference = relativeDF.parse(reference_date);
            Date today_raw = new Date(System.currentTimeMillis());
            Date today_processed = relativeDF.parse(relativeDF.format(today_raw));
            long difference = reference.getTime() - today_processed.getTime();
            difference = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);

            if (difference == 0){
                return  "Today";
            }else if (difference == -1){
                return "Yesterday";
            }else if (difference < -1){
                return (difference * (-1)) + " days ago";
            }else if (difference == 1){
                return "Tomorrow";
            }else if (difference > 1){
                return difference + " days left";
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return  "ERROR";
    }
}
