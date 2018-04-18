package com.intelligent.morning06.lecturemate;


import android.database.Cursor;
import android.database.SQLException;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessNote;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

public class DataBaseAccessNoteInstrumentedTest {
    DataBaseAccessNote dataAccessNote = null;
    DataBaseAccessLecture dataAccessLecture = null;

    @Before
    public void deleteTableIfExists() throws Exception {
        //DataBaseAccessLecture dataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());
        if(dataAccessNote == null)
            dataAccessNote = new DataBaseAccessNote(InstrumentationRegistry.getTargetContext());
        if(dataAccessLecture == null)
            dataAccessLecture = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dataAccessNote.DeleteAllNotes();
        dataAccessLecture.DeleteAllLectures();
    }

    @Test
    public void addNote_LectureDoesNotExist_throwsException() throws Exception {
        Lecture lecture = dataAccessLecture.AddLecture("TestLecture");

        try {
            dataAccessNote.AddNote("TestNodeForTestLecture2", "TestText", System.currentTimeMillis(), 53215);
            Assert.fail("Should have thrown exception");
        } catch(SQLException exception) {
            Log.i("DataBaseAccessNoteInstrumentedTest:addNote_LectureDoesNotExist_throwsException", "ExceptionText= " + exception.getMessage());
        }
    }

    @Test
    public void addNote_LectureDoesExist_isCreatedCorrectly() throws Exception {
        Lecture lecture = dataAccessLecture.AddLecture("TestLecture");

        try {
            dataAccessNote.AddNote("TestNodeForTestLecture", "TestText", System.currentTimeMillis(), lecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor noteCursor = dataAccessNote.GetNoteCursorForLecture(lecture.getId());
        assertEquals(true, noteCursor.moveToFirst());
        String noteText = noteCursor.getString(noteCursor.getColumnIndex(DataBaseAccessNote.NoteTable.COLUMN_NAME_TEXT));
        assertEquals("TestText", noteText);
        assertEquals(false, noteCursor.moveToNext());
    }

    @Test
    public void getNotesForLecture_LectureExists_NotesCorrectlyReturned() throws Exception {
        Lecture firstLecture = dataAccessLecture.AddLecture("TestLecture1");
        Lecture secondLecture = dataAccessLecture.AddLecture("TestLecture2");

        try {
            dataAccessNote.AddNote("TestNoteForTestLecture1_1", "TestText", System.currentTimeMillis(), firstLecture.getId());
            dataAccessNote.AddNote("TestNoteForTestLecture1_1", "TestText", System.currentTimeMillis(), firstLecture.getId());
            dataAccessNote.AddNote("TestNoteForTestLecture2_1", "TestText", System.currentTimeMillis(), secondLecture.getId());
            dataAccessNote.AddNote("TestNoteForTestLecture2_2", "TestText", System.currentTimeMillis(), secondLecture.getId());
            dataAccessNote.AddNote("TestNoteForTestLecture2_3", "TestText", System.currentTimeMillis(), secondLecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor noteCursor = dataAccessNote.GetNoteCursorForLecture(secondLecture.getId());
        int index = 1;
        while(noteCursor.moveToNext()) {
            String noteTitle = noteCursor.getString(noteCursor.getColumnIndex(DataBaseAccessNote.NoteTable.COLUMN_NAME_TITLE));
            assertEquals("TestNoteForTestLecture2_" + index, noteTitle);
            index++;
        }

        if(index == 1)
            Assert.fail("Note cursor didn't contain any notes");
    }

    @Test
    public void getNotesForLecture_LectureExists_NoNotesExist() throws Exception {
        Lecture firstLecture = dataAccessLecture.AddLecture("TestLecture1");
        Lecture secondLecture = dataAccessLecture.AddLecture("TestLecture2");

        try {
            dataAccessNote.AddNote("TestNoteForTestLecture1_1", "TestText", System.currentTimeMillis(), firstLecture.getId());
            dataAccessNote.AddNote("TestNoteForTestLecture1_1", "TestText", System.currentTimeMillis(), firstLecture.getId());
            dataAccessNote.AddNote("TestNoteForTestLecture2_1", "TestText", System.currentTimeMillis(), firstLecture.getId());
            dataAccessNote.AddNote("TestNoteForTestLecture2_2", "TestText", System.currentTimeMillis(), firstLecture.getId());
            dataAccessNote.AddNote("TestNoteForTestLecture2_3", "TestText", System.currentTimeMillis(), firstLecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor noteCursor = dataAccessNote.GetNoteCursorForLecture(2);
        Assert.assertEquals(false, noteCursor.moveToFirst());
    }

}
