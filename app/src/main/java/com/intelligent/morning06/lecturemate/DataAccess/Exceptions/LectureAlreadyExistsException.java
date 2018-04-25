package com.intelligent.morning06.lecturemate.DataAccess.Exceptions;

public class LectureAlreadyExistsException extends Exception {

    private String _lectureName;

    public LectureAlreadyExistsException(String lectureName, String message) {
        super(message);
        _lectureName = lectureName;
    }

    public String getLectureName() {
        return _lectureName;
    }
}
