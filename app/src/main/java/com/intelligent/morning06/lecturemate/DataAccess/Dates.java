package com.intelligent.morning06.lecturemate.DataAccess;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Dates implements Serializable {
    private int _id;
    private String _title;
    private String _text;
    private LocalDateTime _date;
    private LocalDateTime _creationDate;
    private int _lectureId;

    public Dates(int id, String title, String text, LocalDateTime creationDate, int lectureId, LocalDateTime date) {
        _id = id;
        _title = title;
        _text = text;
        _date = date;
        _creationDate = creationDate;
        _lectureId = lectureId;
    }

    public Dates(String title, String text, LocalDateTime creationDate, int lectureId, LocalDateTime date) {
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

    public LocalDateTime getCreationDate() {
        return _creationDate;
    }

    public int getLectureId() {
        return _lectureId;
    }

    public String getTet() { return _text; }

    @Override
    public String toString() {
        return _title;
    }
}