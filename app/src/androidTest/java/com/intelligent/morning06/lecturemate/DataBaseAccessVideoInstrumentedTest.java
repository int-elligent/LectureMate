package com.intelligent.morning06.lecturemate;


import android.database.Cursor;
import android.database.SQLException;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessVideo;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

public class DataBaseAccessVideoInstrumentedTest {
    DataBaseAccessVideo dataAccessVideo = null;
    DataBaseAccessLecture dataAccessLecture = null;

    @Before
    public void deleteTableIfExists() throws Exception {
        if(dataAccessVideo == null)
            dataAccessVideo = new DataBaseAccessVideo(InstrumentationRegistry.getTargetContext());
        if(dataAccessLecture == null)
            dataAccessLecture = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dataAccessVideo.DeleteAllVideos();
        dataAccessLecture.DeleteAllLectures();
    }

    @Test
    public void addVideo_LectureDoesNotExist_throwsException() throws Exception {
        Lecture lecture = dataAccessLecture.AddLecture("TestLecture");

        try {
            dataAccessVideo.AddVideo("TestVideoForTestLecture2", System.currentTimeMillis(), "TestPath", 15325);
            Assert.fail("Should have thrown exception");
        } catch(SQLException exception) {
            Log.i("DataBaseAccessVideoInstrumentedTest:addVideo_LectureDoesNotExist_throwsException", "ExceptionText= " + exception.getMessage());
        }
    }

    @Test
    public void addVideo_LectureDoesExist_isCreatedCorrectly() throws Exception {
        Lecture lecture = dataAccessLecture.AddLecture("TestLecture");

        try {
            dataAccessVideo.AddVideo("TestVideoForTestLecture", System.currentTimeMillis(), "TestPath", lecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor videoCursor = dataAccessVideo.GetVideoCursorForLecture(lecture.getId());
        assertEquals(true, videoCursor.moveToFirst());
        String videoFilePath = videoCursor.getString(videoCursor.getColumnIndex(DataBaseAccessVideo.VideoTable.COLUMN_NAME_FILEPATH));
        assertEquals("TestPath", videoFilePath);
        assertEquals(false, videoCursor.moveToNext());
    }

    @Test
    public void getVideosForLecture_LectureExists_VideosCorrectlyReturned() throws Exception {
        Lecture firstLecture = dataAccessLecture.AddLecture("TestLecture1");
        Lecture secondLecture = dataAccessLecture.AddLecture("TestLecture2");

        try {
            dataAccessVideo.AddVideo("TestVideoForTestLecture1_1", System.currentTimeMillis(), "TestPath", firstLecture.getId());
            dataAccessVideo.AddVideo("TestVideoForTestLecture1_1", System.currentTimeMillis(), "TestPath", firstLecture.getId());
            dataAccessVideo.AddVideo("TestVideoForTestLecture2_1", System.currentTimeMillis(), "TestPath", secondLecture.getId());
            dataAccessVideo.AddVideo("TestVideoForTestLecture2_2", System.currentTimeMillis(), "TestPath", secondLecture.getId());
            dataAccessVideo.AddVideo("TestVideoForTestLecture2_3", System.currentTimeMillis(), "TestPath", secondLecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor videoCursor = dataAccessVideo.GetVideoCursorForLecture(secondLecture.getId());
        int index = 1;
        while(videoCursor.moveToNext()) {
            String videoTitle = videoCursor.getString(videoCursor.getColumnIndex(DataBaseAccessVideo.VideoTable.COLUMN_NAME_TITLE));
            assertEquals("TestVideoForTestLecture2_" + index, videoTitle);
            index++;
        }

        if(index == 1)
            Assert.fail("Video cursor didn't contain any videos");
    }

    @Test
    public void getVideosForLecture_LectureExists_NoVideosExist() throws Exception {
        Lecture firstLecture = dataAccessLecture.AddLecture("TestLecture1");
        Lecture secondLecture = dataAccessLecture.AddLecture("TestLecture2");

        try {
            dataAccessVideo.AddVideo("TestVideoForTestLecture1_1", System.currentTimeMillis(),"TestPath", firstLecture.getId());
            dataAccessVideo.AddVideo("TestVideoForTestLecture1_1", System.currentTimeMillis(),"TestPath", firstLecture.getId());
            dataAccessVideo.AddVideo("TestVideoForTestLecture2_1", System.currentTimeMillis(),"TestPath", firstLecture.getId());
            dataAccessVideo.AddVideo("TestVideoForTestLecture2_2", System.currentTimeMillis(),"TestPath", firstLecture.getId());
            dataAccessVideo.AddVideo("TestVideoForTestLecture2_3", System.currentTimeMillis(),"TestPath", firstLecture.getId());
        } catch(SQLException exception) {
            Assert.fail("Exception shouldn't have been thrown: " + exception.getMessage());
        }

        Cursor videoCursor = dataAccessVideo.GetVideoCursorForLecture(2);
        Assert.assertEquals(false, videoCursor.moveToFirst());
    }

}
