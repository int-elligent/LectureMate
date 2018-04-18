package com.intelligent.morning06.lecturemate;


import android.database.Cursor;
import android.database.SQLException;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessDate;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

public class DataBaseAccessDateInstrumentedTest {
    DataBaseAccessDate dataAccessDate = null;
    DataBaseAccessLecture dataAccessLecture = null;

    @Before
    public void deleteTableIfExists() throws Exception {
        if(dataAccessDate == null)
            dataAccessDate = new DataBaseAccessDate(InstrumentationRegistry.getTargetContext());
        if(dataAccessLecture == null)
            dataAccessLecture = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dataAccessDate.DeleteAllDates();
        dataAccessLecture.DeleteAllLectures();
    }

    @Test
    public void addDate_LectureDoesNotExist_throwsException() throws Exception {
        Lecture lecture = dataAccessLecture.AddLecture("TestLecture");

        try {
            dataAccessDate.AddDate("TestDateForTestLecture2", "TestText", System.currentTimeMillis(), System.currentTimeMillis(), 15325);
            Assert.fail("Should have thrown exception");
        } catch(SQLException exception) {
            Log.i("DataBaseAccessDateInstrumentedTest:addDate_LectureDoesNotExist_throwsException", "ExceptionText= " + exception.getMessage());
        }
    }

    @Test
    public void addDate_LectureDoesExist_isCreatedCorrectly() throws Exception {
        Lecture lecture = dataAccessLecture.AddLecture("TestLecture");

        try {
            dataAccessDate.AddDate("TestDateForTestLecture", "TestText", System.currentTimeMillis(), System.currentTimeMillis(), lecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor dateCursor = dataAccessDate.GetDateCursorForLecture(lecture.getId());
        assertEquals(true, dateCursor.moveToFirst());
        String dateText = dateCursor.getString(dateCursor.getColumnIndex(DataBaseAccessDate.DateTable.COLUMN_NAME_TEXT));
        assertEquals("TestText", dateText);
        assertEquals(false, dateCursor.moveToNext());
    }

    @Test
    public void getDatesForLecture_LectureExists_DatesCorrectlyReturned() throws Exception {
        Lecture firstLecture = dataAccessLecture.AddLecture("TestLecture1");
        Lecture secondLecture = dataAccessLecture.AddLecture("TestLecture2");

        try {
            dataAccessDate.AddDate("TestDateForTestLecture1_1", "TestText", System.currentTimeMillis(),System.currentTimeMillis(), firstLecture.getId());
            dataAccessDate.AddDate("TestDateForTestLecture1_1", "TestText", System.currentTimeMillis(),System.currentTimeMillis(), firstLecture.getId());
            dataAccessDate.AddDate("TestDateForTestLecture2_1", "TestText", System.currentTimeMillis(),System.currentTimeMillis(), secondLecture.getId());
            dataAccessDate.AddDate("TestDateForTestLecture2_2", "TestText", System.currentTimeMillis(),System.currentTimeMillis(), secondLecture.getId());
            dataAccessDate.AddDate("TestDateForTestLecture2_3", "TestText", System.currentTimeMillis(),System.currentTimeMillis(), secondLecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor dateCursor = dataAccessDate.GetDateCursorForLecture(secondLecture.getId());
        int index = 1;
        while(dateCursor.moveToNext()) {
            String dateTitle = dateCursor.getString(dateCursor.getColumnIndex(DataBaseAccessDate.DateTable.COLUMN_NAME_TITLE));
            assertEquals("TestDateForTestLecture2_" + index, dateTitle);
            index++;
        }

        if(index == 1)
            Assert.fail("Date cursor didn't contain any dates");
    }

    @Test
    public void getDatesForLecture_LectureExists_NoDatesExist() throws Exception {
        Lecture firstLecture = dataAccessLecture.AddLecture("TestLecture1");
        Lecture secondLecture = dataAccessLecture.AddLecture("TestLecture2");

        try {
            dataAccessDate.AddDate("TestDateForTestLecture1_1", "TestText", System.currentTimeMillis(),System.currentTimeMillis(), firstLecture.getId());
            dataAccessDate.AddDate("TestDateForTestLecture1_1", "TestText", System.currentTimeMillis(),System.currentTimeMillis(), firstLecture.getId());
            dataAccessDate.AddDate("TestDateForTestLecture2_1", "TestText", System.currentTimeMillis(),System.currentTimeMillis(), firstLecture.getId());
            dataAccessDate.AddDate("TestDateForTestLecture2_2", "TestText", System.currentTimeMillis(),System.currentTimeMillis(), firstLecture.getId());
            dataAccessDate.AddDate("TestDateForTestLecture2_3", "TestText", System.currentTimeMillis(),System.currentTimeMillis(), firstLecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor dateCursor = dataAccessDate.GetDateCursorForLecture(2);
        Assert.assertEquals(false, dateCursor.moveToFirst());
    }

}
