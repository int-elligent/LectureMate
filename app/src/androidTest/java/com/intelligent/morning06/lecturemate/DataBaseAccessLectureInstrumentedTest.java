package com.intelligent.morning06.lecturemate;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureDoesNotExistException;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

public class DataBaseAccessLectureInstrumentedTest {

    DataBaseAccessLecture dataBase = null;

    @Before
    public void deleteTableIfExists() throws Exception {
        //DataBaseAccessLecture dataBaseLecture = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());
        if(dataBase == null)
            dataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dataBase.DeleteAllLectures();
    }

    @Test
    public void addLecture_LectureDoesNotExist_isCreatedCorrectly() throws Exception {
        Lecture lecture = dataBase.AddLecture("Softwaretechnologie");

        List<Lecture> lectures = dataBase.GetAllLectures();

        assertFalse(lecture.getId() == -1);
        assertNotNull(lectures);
        assertEquals(1, lectures.size());
        assertEquals("Softwaretechnologie", lectures.get(0).getLectureName());
    }

    @Test
    public void addLecture_LectureAlreadyExists_throwsLectureAlreadyExistsException() throws Exception {
        dataBase.AddLecture("Softwaretechnologie");

        try {
            dataBase.AddLecture("Softwaretechnologie");
            Assert.fail("Should have thrown LectureAlreadyExistsException.");
        }
        catch(LectureAlreadyExistsException exception) {
            assertEquals("Softwaretechnologie", exception.getLectureName());
        }
    }

    @Test
    public void addLecture_LectureNameEmpty_throwsIllegalArgumentException() throws Exception {
        try {
            dataBase.AddLecture("");
            Assert.fail("Should have thrown IllegalArgumentException.");
        }
        catch(IllegalArgumentException exception) {
            assertEquals("lectureName", exception.getMessage());
        }

        try {
            dataBase.AddLecture(null);
            Assert.fail("Should have thrown IllegalArgumentException.");
        }
        catch(IllegalArgumentException exception) {
            assertEquals("lectureName", exception.getMessage());
        }
    }

    @Test
    public void deleteLecture_LectureExists_isCorrectlyDeleted() throws Exception {
        dataBase.AddLecture("TestLecture");
        dataBase.DeleteLecture("TestLecture");

        List<Lecture> lectures = dataBase.GetAllLectures();
        assertEquals(0, lectures.size());
    }

    @Test
    public void deleteLecture_LectureDoesNotExist_throwsLectureDoesNotExistException() throws Exception {
        try {
            dataBase.DeleteLecture("TestLecture");
            Assert.fail("Should have thrown LectureDoesNotExistException");
        } catch(LectureDoesNotExistException exception) {
            assertEquals("TestLecture", exception.getLectureName());
        }
    }

    @Test
    public void deleteLecture_LectureNameEmpty_throwsIllegalArgumentException() throws Exception {
        try {
            dataBase.DeleteLecture("");
            Assert.fail("Should have thrown IllegalArgumentException");
        } catch(IllegalArgumentException exception) {
            assertEquals("lectureName", exception.getMessage());
        }

        try {
            dataBase.DeleteLecture(null);
            Assert.fail("Should have thrown IllegalArgumentException");
        } catch(IllegalArgumentException exception) {
            assertEquals("lectureName", exception.getMessage());
        }
    }

    @Test
    public void getAllLectures_noLecturesAvailable_returnsEmptyList() throws Exception {
        List<Lecture> lectures = dataBase.GetAllLectures();
        assertEquals(0, lectures.size());
    }

    @Test
    public void getAllLectures_lecturesAvailable_returnsListWithAllLectures() throws Exception {
        dataBase.AddLecture("first");
        dataBase.AddLecture("second");
        dataBase.AddLecture("third");
        dataBase.AddLecture("fourth");

        List<Lecture> lectures = dataBase.GetAllLectures();

        assertEquals(4, lectures.size());
        assertEquals("first", lectures.get(0).getLectureName());
        assertEquals("second", lectures.get(1).getLectureName());
        assertEquals("third", lectures.get(2).getLectureName());
        assertEquals("fourth", lectures.get(3).getLectureName());
    }
}
