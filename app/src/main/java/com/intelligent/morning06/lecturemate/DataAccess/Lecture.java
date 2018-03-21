package com.intelligent.morning06.lecturemate.DataAccess;

/**
 * Created by Marco on 21.03.2018.
 */

public class Lecture {

    private String _lectureName;
    private int _id;

    public Lecture(String lectureName, int id) {
        _lectureName = lectureName;
        _id = id;
    }

    public String getLectureName(){
        return _lectureName;
    }

    public void setLectureName(String lectureName){
        _lectureName = lectureName;
    }

    public int getId(){
        return _id;
    }
}
