package com.intelligent.morning06.lecturemate;

import com.intelligent.morning06.lecturemate.Utils.DateTimeUtils;

import junit.framework.Assert;

import org.junit.Test;

import java.time.LocalDateTime;

public class UtilsTest {

    /*
     public static String FormatDateTimeToMonthAndYear(LocalDateTime dateTime) {
        Date date = Date.from(dateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat dateFormat = new SimpleDateFormat( "LLLL yyyy", Locale.getDefault() );
        return dateFormat.format(date);
    }

    public static String FormatDateTimeAsNormalDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return formatter.format(dateTime);
    }
     */

    @Test
    public void testFormatDateTimeToMonthAndYear() throws Exception {
        LocalDateTime testDateTime = LocalDateTime.parse("2007-12-03T10:15:30");
        String dateString = DateTimeUtils.FormatDateTimeToMonthAndYear(testDateTime);
        testDateTime = LocalDateTime.parse("2018-01-27T00:00:00");
        String dateStringTwo = DateTimeUtils.FormatDateTimeToMonthAndYear(testDateTime);
        Assert.assertEquals("Dezember 2007", dateString);
        Assert.assertEquals("Jänner 2018", dateStringTwo);
    }

    @Test
    public void testFormatDateTimeAsNormalDate() throws Exception {
        LocalDateTime testDateTime = LocalDateTime.parse("2007-12-03T10:15:30");
        String dateString = DateTimeUtils.FormatDateTimeAsNormalDate(testDateTime);
        testDateTime = LocalDateTime.parse("2018-01-27T00:00:00");
        String dateStringTwo = DateTimeUtils.FormatDateTimeAsNormalDate(testDateTime);

        Assert.assertEquals("03.12.2007", dateString);
        Assert.assertEquals("27.01.2018", dateStringTwo);
    }
}
