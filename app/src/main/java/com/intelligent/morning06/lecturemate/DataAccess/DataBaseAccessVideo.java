package com.intelligent.morning06.lecturemate.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseAccessVideo extends SQLiteOpenHelper  {

    public static class VideoTable implements BaseColumns {
        public static final String TABLE_NAME = "videos";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CREATIONDATE = "creationdate";
        public static final String COLUMN_NAME_LECTUREID = "lectureid";
        public static final String COLUMN_NAME_FILEPATH = "filepath";
    }

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + VideoTable.TABLE_NAME + " (" +
                    VideoTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    VideoTable.COLUMN_NAME_TITLE + " TEXT, " +
                    VideoTable.COLUMN_NAME_CREATIONDATE + " INTEGER, " +
                    VideoTable.COLUMN_NAME_FILEPATH + " Text, " +
                    VideoTable.COLUMN_NAME_LECTUREID + " INTEGER, " +
                    "FOREIGN KEY (" +
                    VideoTable.COLUMN_NAME_LECTUREID + ") REFERENCES " +
                    DataBaseAccessLecture.LectureTable.TABLE_NAME + "(" +
                    DataBaseAccessLecture.LectureTable.COLUMN_NAME_ID + "));";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + VideoTable.TABLE_NAME;

    public DataBaseAccessVideo(Context context) {
        super(context, DataBaseConstants.DATABASE_NAME, null, DataBaseConstants.DATABASE_VERSION);
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

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public void AddVideo(String videoTitle, long timeStampCreated, String filePath, int lectureId) throws SQLException {
        SQLiteDatabase dataBase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VideoTable.COLUMN_NAME_TITLE, videoTitle);
        values.put(VideoTable.COLUMN_NAME_CREATIONDATE, timeStampCreated);
        values.put(VideoTable.COLUMN_NAME_LECTUREID, lectureId);
        values.put(VideoTable.COLUMN_NAME_FILEPATH, filePath);
        dataBase.insertOrThrow(VideoTable.TABLE_NAME, null, values);
        dataBase.close();
    }

    public Cursor GetVideoCursorForLecture(int lectureId) {
        SQLiteDatabase dataBase = this.getReadableDatabase();

        String[] columns = {
                VideoTable.COLUMN_NAME_ID,
                VideoTable.COLUMN_NAME_TITLE,
                VideoTable.COLUMN_NAME_CREATIONDATE,
                VideoTable.COLUMN_NAME_LECTUREID,
                VideoTable.COLUMN_NAME_FILEPATH
        };

        Cursor cursor = dataBase.query(VideoTable.TABLE_NAME,
                columns,
                VideoTable.COLUMN_NAME_LECTUREID + "=?",
                new String[] {"" + lectureId},
                null,
                null,
                null);

        return cursor;
    }

    public void DeleteAllVideos() {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        dataBase.delete(VideoTable.TABLE_NAME, null, null);
        dataBase.close();
    }

    public void DeleteVideosForLectureId(int lectureId) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        dataBase.delete(VideoTable.TABLE_NAME, VideoTable.COLUMN_NAME_LECTUREID + "=?", new String[] {"" + lectureId});
        dataBase.close();
    }


}
