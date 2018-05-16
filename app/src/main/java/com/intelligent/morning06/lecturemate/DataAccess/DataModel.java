package com.intelligent.morning06.lecturemate.DataAccess;

import com.intelligent.morning06.lecturemate.LecturesActivity;
        import com.intelligent.morning06.lecturemate.MyApplication;

        import java.util.List;

public class DataModel {

    private static DataModel _instance = null;
    private DataBaseAccessLecture _lectureDataBase = null;
    private DataBaseAccessNote _noteDataBase = null;
    private DataBaseAccessImage _imageDataBase = null;

    private List<Lecture> _allLectures;

    private DataModel() {
        _lectureDataBase = new DataBaseAccessLecture(MyApplication.getContext());
        _noteDataBase = new DataBaseAccessNote(MyApplication.getContext());
        _imageDataBase = new DataBaseAccessImage(MyApplication.getContext());
    }

    public static DataModel GetInstance() {
        if(_instance == null)
            _instance = new DataModel();

        return _instance;
    }

    public DataBaseAccessLecture getLectureDataBase() {
        return _lectureDataBase;
    }

    public DataBaseAccessNote getNoteDataBase() { return _noteDataBase; }

    public DataBaseAccessImage getImageDataBase() { return _imageDataBase; }

}
