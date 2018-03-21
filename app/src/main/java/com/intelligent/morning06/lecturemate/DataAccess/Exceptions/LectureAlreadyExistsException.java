package com.intelligent.morning06.lecturemate.DataAccess.Exceptions;

/**
 * Created by Marco on 21.03.2018.
 */

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
