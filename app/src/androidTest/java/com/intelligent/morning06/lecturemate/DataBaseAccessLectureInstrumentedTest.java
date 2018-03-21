package com.intelligent.morning06.lecturemate;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Marco on 21.03.2018.
 */

public class DataBaseAccessLectureInstrumentedTest {

    DataBaseAccessLecture dataBase = null;

    @Before
    public void deleteTableIfExists() throws Exception {
        //DataBaseAccessLecture dataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());
        if(dataBase == null)
            dataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dataBase.deleteAllLectures();
    }

    @Test
    public void addLecture_LectureDoesNotExist_isCreatedCorrectly() throws Exception {
        Lecture lecture = dataBase.AddLecture("Softwaretechnologie");

        List<Lecture> lectures = dataBase.GetAllLectures();

        assertFalse(lecture.getId() == -1);
        assertNotNull(lectures);
        assertEquals(1, lectures.size());
        assertEquals(lectures.get(0).getLectureName(), "Softwaretechnologie");
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

    public void addLecture_LectureNameEmpty_throwsIllegalArgumentException() throws Exception {

    }

    @Test
    public void deleteLecture_LectureExists_isCorrectlyDeleted() throws Exception {

    }
}
