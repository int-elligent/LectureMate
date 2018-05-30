package com.intelligent.morning06.lecturemate.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseAccessDate extends SQLiteOpenHelper  {

    public static class DateTable implements BaseColumns {
        public static final String TABLE_NAME = "dates";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_CREATIONDATE = "creationdate";
        public static final String COLUMN_NAME_LECTUREID = "lectureid";
        public static final String COLUMN_NAME_DATE = "date";
    }

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DateTable.TABLE_NAME + " (" +
                    DateTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DateTable.COLUMN_NAME_TITLE + " TEXT, " +
                    DateTable.COLUMN_NAME_TEXT + " TEXT, " +
                    DateTable.COLUMN_NAME_CREATIONDATE + " INTEGER, " +
                    DateTable.COLUMN_NAME_DATE + " INTEGER, " +
                    DateTable.COLUMN_NAME_LECTUREID + " INTEGER, " +
                    "FOREIGN KEY (" +
                    DateTable.COLUMN_NAME_LECTUREID + ") REFERENCES " +
                    DataBaseAccessLecture.LectureTable.TABLE_NAME + "(" +
                    DataBaseAccessLecture.LectureTable.COLUMN_NAME_ID + "));";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + DateTable.TABLE_NAME;

    public DataBaseAccessDate(Context context) {
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
    }

    public void AddDate(String dateTitle, String dateText, long timeStampCreated, long timeStampDate, int lectureId) throws SQLException {
        SQLiteDatabase dataBase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DateTable.COLUMN_NAME_TITLE, dateTitle);
        values.put(DateTable.COLUMN_NAME_TEXT, dateText);
        values.put(DateTable.COLUMN_NAME_CREATIONDATE, timeStampCreated);
        values.put(DateTable.COLUMN_NAME_LECTUREID, lectureId);
        values.put(DateTable.COLUMN_NAME_DATE, timeStampDate);
        dataBase.insertOrThrow(DateTable.TABLE_NAME, null, values);
        dataBase.close();
    }

    public void AddDate(Dates dateObj) throws SQLException{
        SQLiteDatabase dataBase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DateTable.COLUMN_NAME_TITLE, dateObj.getTitle());
        values.put(DateTable.COLUMN_NAME_TEXT, dateObj.getText());
        values.put(DateTable.COLUMN_NAME_CREATIONDATE, dateObj.getCreationDateLong());
        values.put(DateTable.COLUMN_NAME_LECTUREID, dateObj.getLectureId());
        values.put(DateTable.COLUMN_NAME_DATE, dateObj.getDateLong());
        dataBase.insertOrThrow(DateTable.TABLE_NAME, null, values);
        dataBase.close();
    }

    public Cursor GetDateCursorForLecture(int lectureId) {
        SQLiteDatabase dataBase = this.getReadableDatabase();

        String[] columns = {
                DateTable.COLUMN_NAME_ID,
                DateTable.COLUMN_NAME_TITLE,
                DateTable.COLUMN_NAME_TEXT,
                DateTable.COLUMN_NAME_CREATIONDATE,
                DateTable.COLUMN_NAME_DATE,
                DateTable.COLUMN_NAME_LECTUREID
        };

        Cursor cursor = dataBase.query(DateTable.TABLE_NAME,
                columns,
                DateTable.COLUMN_NAME_LECTUREID + "=?",
                new String[] {"" + lectureId},
                null,
                null,
                null);

        return cursor;
    }

    public void DeleteAllDates() {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        dataBase.delete(DateTable.TABLE_NAME, null, null);
        dataBase.close();
    }

    public void DeleteDatesForLectureId(int lectureId) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        dataBase.delete(DateTable.TABLE_NAME, DateTable.COLUMN_NAME_LECTUREID + "=?", new String[] {"" + lectureId});
        dataBase.close();
    }


}
