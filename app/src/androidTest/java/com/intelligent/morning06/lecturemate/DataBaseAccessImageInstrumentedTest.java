package com.intelligent.morning06.lecturemate;


import android.database.Cursor;
import android.database.SQLException;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessImage;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

public class DataBaseAccessImageInstrumentedTest {
    DataBaseAccessImage dataAccessImage = null;
    DataBaseAccessLecture dataAccessLecture = null;

    @Before
    public void deleteTableIfExists() throws Exception {
        if(dataAccessImage == null)
            dataAccessImage = new DataBaseAccessImage(InstrumentationRegistry.getTargetContext());
        if(dataAccessLecture == null)
            dataAccessLecture = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dataAccessImage.DeleteAllImages();
        dataAccessLecture.DeleteAllLectures();
    }

    @Test
    public void addImage_LectureDoesNotExist_throwsException() throws Exception {
        Lecture lecture = dataAccessLecture.AddLecture("TestLecture");

        try {
            dataAccessImage.AddImage("TestImageForTestLecture2", System.currentTimeMillis(), "TestPath", 15325);
            Assert.fail("Should have thrown exception");
        } catch(SQLException exception) {
            Log.i("DataBaseAccessImageInstrumentedTest:addImage_LectureDoesNotExist_throwsException", "ExceptionText= " + exception.getMessage());
        }
    }

    @Test
    public void addImage_LectureDoesExist_isCreatedCorrectly() throws Exception {
        Lecture lecture = dataAccessLecture.AddLecture("TestLecture");

        try {
            dataAccessImage.AddImage("TestImageForTestLecture", System.currentTimeMillis(), "TestPath", lecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor imageCursor = dataAccessImage.GetImageCursorForLecture(lecture.getId());
        assertEquals(true, imageCursor.moveToFirst());
        String imageFilePath = imageCursor.getString(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_FILEPATH));
        assertEquals("TestPath", imageFilePath);
        assertEquals(false, imageCursor.moveToNext());
    }

    @Test
    public void getImagesForLecture_LectureExists_ImagesCorrectlyReturned() throws Exception {
        Lecture firstLecture = dataAccessLecture.AddLecture("TestLecture1");
        Lecture secondLecture = dataAccessLecture.AddLecture("TestLecture2");

        try {
            dataAccessImage.AddImage("TestImageForTestLecture1_1", System.currentTimeMillis(), "TestPath", firstLecture.getId());
            dataAccessImage.AddImage("TestImageForTestLecture1_1", System.currentTimeMillis(), "TestPath", firstLecture.getId());
            dataAccessImage.AddImage("TestImageForTestLecture2_1", System.currentTimeMillis(), "TestPath", secondLecture.getId());
            dataAccessImage.AddImage("TestImageForTestLecture2_2", System.currentTimeMillis(), "TestPath", secondLecture.getId());
            dataAccessImage.AddImage("TestImageForTestLecture2_3", System.currentTimeMillis(), "TestPath", secondLecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor imageCursor = dataAccessImage.GetImageCursorForLecture(secondLecture.getId());
        int index = 1;
        while(imageCursor.moveToNext()) {
            String imageTitle = imageCursor.getString(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_TITLE));
            assertEquals("TestImageForTestLecture2_" + index, imageTitle);
            index++;
        }

        if(index == 1)
            Assert.fail("Image cursor didn't contain any images");
    }

    @Test
    public void getImagesForLecture_LectureExists_NoImagesExist() throws Exception {
        Lecture firstLecture = dataAccessLecture.AddLecture("TestLecture1");
        Lecture secondLecture = dataAccessLecture.AddLecture("TestLecture2");

        try {
            dataAccessImage.AddImage("TestImageForTestLecture1_1", System.currentTimeMillis(),"TestPath", firstLecture.getId());
            dataAccessImage.AddImage("TestImageForTestLecture1_1", System.currentTimeMillis(),"TestPath", firstLecture.getId());
            dataAccessImage.AddImage("TestImageForTestLecture2_1", System.currentTimeMillis(),"TestPath", firstLecture.getId());
            dataAccessImage.AddImage("TestImageForTestLecture2_2", System.currentTimeMillis(),"TestPath", firstLecture.getId());
            dataAccessImage.AddImage("TestImageForTestLecture2_3", System.currentTimeMillis(),"TestPath", firstLecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor imageCursor = dataAccessImage.GetImageCursorForLecture(2);
        Assert.assertEquals(false, imageCursor.moveToFirst());
    }

}
