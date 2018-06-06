package com.intelligent.morning06.lecturemate.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.database.sqlite.SQLiteOpenHelper;

import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.ItemDoesNotExistException;

public class DataBaseAccessNote extends SQLiteOpenHelper  {

    public static class NoteTable implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_CREATIONDATE = "creationdate";
        public static final String COLUMN_NAME_LECTUREID = "lectureid";
    }

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + NoteTable.TABLE_NAME + " (" +
                    NoteTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NoteTable.COLUMN_NAME_TITLE + " TEXT, " +
                    NoteTable.COLUMN_NAME_TEXT + " TEXT, " +
                    NoteTable.COLUMN_NAME_CREATIONDATE + " INTEGER, " +
                    NoteTable.COLUMN_NAME_LECTUREID + " INTEGER, " +
                    "FOREIGN KEY (" +
                    NoteTable.COLUMN_NAME_LECTUREID + ") REFERENCES " +
                    DataBaseAccessLecture.LectureTable.TABLE_NAME + "(" +
                    DataBaseAccessLecture.LectureTable.COLUMN_NAME_ID + "));";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + NoteTable.TABLE_NAME;

    public DataBaseAccessNote(Context context) {
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

    public void AddNote(String noteTitle, String noteText, long timeStamp, int lectureId) throws SQLException {
        SQLiteDatabase dataBase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteTable.COLUMN_NAME_TITLE, noteTitle);
        values.put(NoteTable.COLUMN_NAME_TEXT, noteText);
        values.put(NoteTable.COLUMN_NAME_CREATIONDATE, timeStamp);
        values.put(NoteTable.COLUMN_NAME_LECTUREID, lectureId);
        dataBase.insertOrThrow(NoteTable.TABLE_NAME, null, values);
        dataBase.close();
    }

    public Cursor GetNoteCursorForLecture(int lectureId) {
        SQLiteDatabase dataBase = this.getReadableDatabase();

        String[] columns = {
                NoteTable.COLUMN_NAME_ID,
                NoteTable.COLUMN_NAME_TITLE,
                NoteTable.COLUMN_NAME_TEXT,
                NoteTable.COLUMN_NAME_CREATIONDATE,
                NoteTable.COLUMN_NAME_LECTUREID
        };

        Cursor cursor = dataBase.query(NoteTable.TABLE_NAME,
                         columns,
                NoteTable.COLUMN_NAME_LECTUREID + "=?",
                         new String[] {"" + lectureId},
                null,
                 null,
                null);

        return cursor;
    }

    public void DeleteNote(String noteName) throws ItemDoesNotExistException, IllegalArgumentException {

        if(noteName == null || noteName.isEmpty()) {
            throw new IllegalArgumentException("lectureName");
        }

        SQLiteDatabase dataBase = this.getWritableDatabase();

        int numberDeletedRows = dataBase.delete(DataBaseAccessNote.NoteTable.TABLE_NAME, DataBaseAccessNote.NoteTable.COLUMN_NAME_TITLE + "='" + noteName + "'" ,null);
        if(numberDeletedRows == 0) {
            throw new ItemDoesNotExistException(noteName, "Lecture with name '" + noteName + "' cannot be deleted because it does not exist.");
        }
    }


    public void DeleteAllNotes() {
        try {

            SQLiteDatabase dataBase = this.getWritableDatabase();
            dataBase.delete(NoteTable.TABLE_NAME, null, null);
            dataBase.close();

        }catch(SQLException exception) {
            //We don't care about "no such table" exception here
            ;
        }
    }

    public void DeleteNotesForLectureId(int lectureId) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        dataBase.delete(NoteTable.TABLE_NAME, NoteTable.COLUMN_NAME_LECTUREID + "=?", new String[] {"" + lectureId});
        dataBase.close();
    }


}
