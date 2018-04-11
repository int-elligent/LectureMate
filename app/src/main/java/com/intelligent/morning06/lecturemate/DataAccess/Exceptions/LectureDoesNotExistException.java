package com.intelligent.morning06.lecturemate.DataAccess.Exceptions;

/**
 * Created by Marco on 11.04.2018.
 */

public class LectureDoesNotExistException extends Exception {

    private String _lectureName;

    public LectureDoesNotExistException(String lectureName, String message) {
        super(message);
        _lectureName = lectureName;
    }

    public String getLectureName() {
        return _lectureName;
    }
}

