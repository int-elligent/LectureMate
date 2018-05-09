package com.intelligent.morning06.lecturemate.DataAccess;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Image implements Serializable {
    private int _id;
    private String _title;
    private String _filePath;
    private LocalDateTime _creationDate;
    private int _lectureId;

    public Image(int id, String title, String filePath, LocalDateTime creationDate, int lectureId) {
        _id = id;
        _title = title;
        _filePath = filePath;
        _creationDate = creationDate;
        _lectureId = lectureId;
    }

    public Image(String title, String filePath, LocalDateTime creationDate, int lectureId) {
        _title = title;
        _filePath = filePath;
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

    public String getFilePath() {
        return _filePath;
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