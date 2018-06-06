package com.intelligent.morning06.lecturemate.DataAccess.Exceptions;

public class ItemDoesNotExistException extends Exception {

    private String _lectureName;

    public ItemDoesNotExistException(String lectureName, String message) {
        super(message);
        _lectureName = lectureName;
    }

    public String getLectureName() {
        return _lectureName;
    }
}

