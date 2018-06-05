package com.intelligent.morning06.lecturemate.DataAccess;

import com.intelligent.morning06.lecturemate.Interfaces.IDateSortable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

public class MyDate implements Serializable, IDateSortable {
    private int _id;
    private String _title;
    private String _text;
    private LocalDateTime _date;
    private LocalDateTime _creationDate;
    private int _lectureId;

    public MyDate(int id, String title, String text, LocalDateTime creationDate, int lectureId, LocalDateTime date) {
        _id = id;
        _title = title;
        _text = text;
        _date = date;
        _creationDate = creationDate;
        _lectureId = lectureId;
    }

    public MyDate(String title, String text, LocalDateTime creationDate, int lectureId, LocalDateTime date) {
        _title = title;
        _date = date;
        _text = text;
        _creationDate = creationDate;
        _lectureId = lectureId;
        _id = -1;
    }


    public int getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public LocalDateTime getDate() {
        return _date;
    }

    public long getDateLong() {
        return _date.atZone(TimeZone.getDefault().toZoneId()).toInstant().toEpochMilli();
    }

    public LocalDateTime getCreationDate() {
        return _creationDate;
    }

    public long getCreationDateLong() {
        return _creationDate.atZone(TimeZone.getDefault().toZoneId()).toInstant().toEpochMilli();
    }

    public int getLectureId() {
        return _lectureId;
    }

    public String getText() { return _text; }

    public void setText(String text){ _text = text; }

    @Override
    public String toString() {
        return _title;
    }
}