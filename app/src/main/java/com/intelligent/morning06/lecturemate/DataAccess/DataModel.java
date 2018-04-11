package com.intelligent.morning06.lecturemate.DataAccess;

import com.intelligent.morning06.lecturemate.LecturesActivity;
import com.intelligent.morning06.lecturemate.MyApplication;

import java.util.List;

public class DataModel {

    private static DataModel _instance = null;
    private DataBaseAccessLecture _lectureDataBase = null;

    private List<Lecture> _allLectures;

    private DataModel() {
        _lectureDataBase = new DataBaseAccessLecture(MyApplication.getContext());
    }

    public static DataModel GetInstance() {
        if(_instance == null)
            _instance = new DataModel();

        return _instance;
    }

    public DataBaseAccessLecture getDataBase() {
        return _lectureDataBase;
    }

}
