package com.intelligent.morning06.lecturemate.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.database.sqlite.SQLiteOpenHelper;

import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureDoesNotExistException;
import com.intelligent.morning06.lecturemate.MyApplication;
import com.intelligent.morning06.lecturemate.R;

import java.util.ArrayList;
import java.util.List;

public class DataBaseAccessLecture extends SQLiteOpenHelper  {

    public static class LectureTable implements BaseColumns {
        public static final String TABLE_NAME = "lectures";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
    }

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + LectureTable.TABLE_NAME + " (" +
                    LectureTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LectureTable.COLUMN_NAME_TITLE + " TEXT)";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + LectureTable.TABLE_NAME;

    public DataBaseAccessLecture(Context context) {
        super(context, DataBaseConstants.DATABASE_NAME, null, DataBaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        onCreate(db);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void DeleteAllLectures()
    {
        SQLiteDatabase dataBase = this.getWritableDatabase();

        dataBase.delete(LectureTable.TABLE_NAME, null, null);
    }

    public void DeleteLecture(String lectureName) throws LectureDoesNotExistException, IllegalArgumentException {

        if(lectureName == null || lectureName.isEmpty()) {
            throw new IllegalArgumentException("lectureName");
        }

        SQLiteDatabase dataBase = this.getWritableDatabase();

        int numberDeletedRows = dataBase.delete(LectureTable.TABLE_NAME, LectureTable.COLUMN_NAME_TITLE + "='" + lectureName + "'" ,null);
        if(numberDeletedRows == 0) {
            throw new LectureDoesNotExistException(lectureName, "Lecture with name '" + lectureName + "' cannot be deleted because it does not exist.");
        }
    }

    public Lecture AddLecture(String lectureName) throws LectureAlreadyExistsException, IllegalArgumentException {

        if(lectureName == null || lectureName.isEmpty()) {
            throw new IllegalArgumentException("lectureName");
        }

        SQLiteDatabase dataBase = this.getReadableDatabase();

        String[] projection = {
                LectureTable.COLUMN_NAME_TITLE
        };

        String selection = LectureTable.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { lectureName };

        Cursor cursor = dataBase.query(
                LectureTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        List<String> storedLectureNames = new ArrayList<String>();
        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(LectureTable.COLUMN_NAME_TITLE));
            if(title != null)
                storedLectureNames.add(title);
        }

        if(storedLectureNames.size() >= 1) {
            dataBase.close();
            throw new LectureAlreadyExistsException(lectureName, MyApplication.getContext().getResources().getString(R.string.error_databaseAccessLecture_alreadyExists));
        }

        dataBase.close();
        dataBase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LectureTable.COLUMN_NAME_TITLE, lectureName);
        long id = dataBase.insert(LectureTable.TABLE_NAME, null, values);
        Lecture lecture = new Lecture(lectureName, (int)id);
        dataBase.close();
        return lecture;
    }

    public List<Lecture> GetAllLectures() {

        SQLiteDatabase dataBase = this.getReadableDatabase();

        String[] projection = {
                LectureTable.COLUMN_NAME_ID,
                LectureTable.COLUMN_NAME_TITLE
        };

        Cursor cursor = dataBase.query(
                LectureTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Lecture> lectures = new ArrayList<Lecture>();
        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(LectureTable.COLUMN_NAME_TITLE));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(LectureTable.COLUMN_NAME_ID));
            Lecture lecture = new Lecture(title, id);
            lectures.add(lecture);
        }
        return lectures;
    }


}
