package com.intelligent.morning06.lecturemate;


import com.intelligent.morning06.lecturemate.Utils.DateTimeUtils;

import junit.framework.Assert;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class UtilsTest {

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

    @Test
    public void testGetRelativeDate() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime past = now.minusDays(5);
        LocalDateTime future = now.plusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Assert.assertEquals("Today", DateTimeUtils.getRelativeDate(now.format(formatter)));
        Assert.assertEquals("Yesterday", DateTimeUtils.getRelativeDate(yesterday.format(formatter)));
        Assert.assertEquals("Tomorrow", DateTimeUtils.getRelativeDate(tomorrow.format(formatter)));
        Assert.assertEquals("5 days ago", DateTimeUtils.getRelativeDate(past.format(formatter)));
        Assert.assertEquals("5 days left", DateTimeUtils.getRelativeDate(future.format(formatter)));
        Assert.assertEquals("ERROR", DateTimeUtils.getRelativeDate("blabla"));
    }
}
