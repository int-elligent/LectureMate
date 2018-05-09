package com.intelligent.morning06.lecturemate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessImage;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ImagesListActivityInstrumentedTest {
    private DataBaseAccessImage imageDataBase = null;
    private DataBaseAccessLecture lectureDataBase = null;
    private int lectureId = 0;

    private LocalDateTime firstDate;
    private LocalDateTime secondDate;

    @Rule
    public ActivityTestRule<ImagesListActivity> mActivityRule =
            new ActivityTestRule<ImagesListActivity>(ImagesListActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            try {
                setUp();
            }catch(Exception exception) {
                Log.d("test", exception.getMessage());
            }


            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, ImagesListActivity.class);
            return result;
        }};

    public void setUp() throws Exception {
        if (imageDataBase == null)
            imageDataBase = new DataBaseAccessImage(InstrumentationRegistry.getTargetContext());
        if (lectureDataBase == null)
            lectureDataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        imageDataBase.DeleteAllImages();
        lectureDataBase.DeleteAllLectures();

        try {
            Lecture lecture = DataModel.GetInstance().getLectureDataBase().AddLecture("TestLecture");
            lectureId = lecture.getId();
            MyApplication.setCurrentLecture(lectureId);
        } catch (LectureAlreadyExistsException exception) {
            Assert.fail("Lecture already exists. Should not happen");
        }

        insertSampleImages();
    }

    private void insertSampleImages() {
        firstDate = LocalDateTime.of(2010, 12, 10, 5, 32);
        secondDate = LocalDateTime.of(2010, 10, 5, 5, 22);
        String temporaryFilePath;

        Bitmap testImage = BitmapFactory.decodeResource(InstrumentationRegistry.getTargetContext().getResources(), R.mipmap.app_icon);
        try {
            File temporaryFile = File.createTempFile("testImage", "tmp", InstrumentationRegistry.getTargetContext().getCacheDir());

            OutputStream fileStream = new FileOutputStream(temporaryFile);
            testImage.compress(Bitmap.CompressFormat.PNG, 10, fileStream);
            temporaryFilePath = temporaryFile.getAbsolutePath();
        } catch(IOException ioException) {
            Assert.fail("Could not create temporary image for testing purposes: " + ioException.getMessage());
            return;
        }



        for(int i=0; i < 4; i++) {
            LocalDateTime creationDate;
            if(i % 2 == 0) {
                creationDate = firstDate;
            } else {
                creationDate = secondDate;
            }

            int lectureId = MyApplication.getCurrentLecture();
            imageDataBase.AddImage("TestImage" + i, creationDate.toInstant(ZoneOffset.UTC).toEpochMilli(), temporaryFilePath, MyApplication.getCurrentLecture());
        }
    }

    @Test
    public void sampleImagesAreVisible() throws Exception {
        onView(withText("TestImage2")).perform(click());
        onView(withText("TestImage3")).perform(click());
    }

    @Test
    public void sampleSeparatorsAreVisible() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String firstDateString = firstDate.format(formatter);
        String secondDateString = secondDate.format(formatter);

        onView(withText(firstDateString)).check(matches(isDisplayed()));
        onView(withText(secondDateString)).check(matches(isDisplayed()));
    }
}
