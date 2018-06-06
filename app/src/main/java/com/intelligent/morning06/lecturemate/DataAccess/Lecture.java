package com.intelligent.morning06.lecturemate.DataAccess;


import java.io.Serializable;

public class Lecture implements Serializable {

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

    @Override
    public String toString() {
        return _lectureName;
    }
}
