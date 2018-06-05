package com.intelligent.morning06.lecturemate.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseAccessImage extends SQLiteOpenHelper  {

    public static class ImageTable implements BaseColumns {
        public static final String TABLE_NAME = "images";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CREATIONDATE = "creationdate";
        public static final String COLUMN_NAME_LECTUREID = "lectureid";
        public static final String COLUMN_NAME_FILEPATH = "filepath";
    }

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ImageTable.TABLE_NAME + " (" +
                    ImageTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ImageTable.COLUMN_NAME_TITLE + " TEXT, " +
                    ImageTable.COLUMN_NAME_CREATIONDATE + " INTEGER, " +
                    ImageTable.COLUMN_NAME_FILEPATH + " Text, " +
                    ImageTable.COLUMN_NAME_LECTUREID + " INTEGER, " +
                    "FOREIGN KEY (" +
                    ImageTable.COLUMN_NAME_LECTUREID + ") REFERENCES " +
                    DataBaseAccessLecture.LectureTable.TABLE_NAME + "(" +
                    DataBaseAccessLecture.LectureTable.COLUMN_NAME_ID + "));";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + ImageTable.TABLE_NAME;

    public DataBaseAccessImage(Context context) {
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
        onCreate(db);
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
        else {

        }
    }

    public void AddImage(String imageTitle, long timeStampCreated, String filePath, int lectureId) throws SQLException {
        SQLiteDatabase dataBase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ImageTable.COLUMN_NAME_TITLE, imageTitle);
        values.put(ImageTable.COLUMN_NAME_CREATIONDATE, timeStampCreated);
        values.put(ImageTable.COLUMN_NAME_LECTUREID, lectureId);
        values.put(ImageTable.COLUMN_NAME_FILEPATH, filePath);
        dataBase.insertOrThrow(ImageTable.TABLE_NAME, null, values);
        dataBase.close();
    }

    public Cursor GetImageCursorForLecture(int lectureId) {
        SQLiteDatabase dataBase = this.getReadableDatabase();

        String[] columns = {
                ImageTable.COLUMN_NAME_ID,
                ImageTable.COLUMN_NAME_TITLE,
                ImageTable.COLUMN_NAME_CREATIONDATE,
                ImageTable.COLUMN_NAME_LECTUREID,
                ImageTable.COLUMN_NAME_FILEPATH
        };

        Cursor cursor = dataBase.query(ImageTable.TABLE_NAME,
                columns,
                ImageTable.COLUMN_NAME_LECTUREID + "=?",
                new String[] {"" + lectureId},
                null,
                null,
                null);

        return cursor;
    }

    public void DeleteAllImages() {
        try {

            SQLiteDatabase dataBase = this.getWritableDatabase();
            dataBase.delete(ImageTable.TABLE_NAME, null, null);
            dataBase.close();

        }catch(SQLException exception) {
            //We don't care about "no such table" exception here
            ;
        }
    }

    public void DeleteImagesForLectureId(int lectureId) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        dataBase.delete(ImageTable.TABLE_NAME, ImageTable.COLUMN_NAME_LECTUREID + "=?", new String[] {"" + lectureId});
        dataBase.close();
    }


}
