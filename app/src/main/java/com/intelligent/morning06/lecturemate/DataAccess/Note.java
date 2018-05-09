package com.intelligent.morning06.lecturemate.DataAccess;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Note implements Serializable {
    private int _id;
    private String _title;
    private String _text;
    private LocalDateTime _creationDate;
    private int _lectureId;

    public Note(int id, String title, String text, LocalDateTime creationDate, int lectureId) {
        _id = id;
        _title = title;
        _text = text;
        _creationDate = creationDate;
        _lectureId = lectureId;
    }

    public Note(String title, String text, LocalDateTime creationDate, int lectureId) {
        _title = title;
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

    public String getText() {
        return _text;
    }

    public LocalDateTime getCreationDate() {
        return _creationDate;
    }

    public int getLectureId() {
        return _lectureId;
    }

    @Override
    public String toString() {
        return _title;
    }
}