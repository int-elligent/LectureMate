package com.intelligent.morning06.lecturemate;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication mContext;
    private static int _currentlySelectedLecture;
    private static String _currentLectureName;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static MyApplication getContext() {
        return mContext;
    }
    public static int getCurrentLecture() { return _currentlySelectedLecture; }
    public static void setCurrentLecture(int lectureId) { _currentlySelectedLecture = lectureId; }

    public static String getCurrentLectureName() { return _currentLectureName; }
    public static void setCurrentLectureName(String currentLectureName) { _currentLectureName = currentLectureName; }
}
