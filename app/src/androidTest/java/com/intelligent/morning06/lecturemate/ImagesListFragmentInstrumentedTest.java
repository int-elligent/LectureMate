package com.intelligent.morning06.lecturemate;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessImage;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;
import com.intelligent.morning06.lecturemate.ListFragments.ImagesListFragment;
import com.intelligent.morning06.lecturemate.Utils.DateTimeUtils;

import junit.framework.Assert;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
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
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class ImagesListFragmentInstrumentedTest {
    private DataBaseAccessImage imageDataBase = null;
    private DataBaseAccessLecture lectureDataBase = null;
    private int lectureId = 0;

    private LocalDateTime firstDate;
    private LocalDateTime secondDate;

    private Uri _imageUri;

    @Rule
    public ActivityTestRule<TabCategoriesActivity> mActivityRule = new
            ActivityTestRule<TabCategoriesActivity>(TabCategoriesActivity.class, true, false);

    @Rule
    public GrantPermissionRule mRuntimePermissionRuleReadStorage = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule mRuntimePermissionRuleWriteStorage = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Before
    public void navigateToImageListFragment() throws Exception {
        setUp();
        MyApplication.setStoragePermissionGranted(true);
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
        onView(withText("Images")).perform(click());
    }


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
            MyApplication.setCurrentLectureName(lecture.getLectureName());
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
        File temporaryFile;
        try {
            temporaryFile = File.createTempFile("testImage", "tmp", InstrumentationRegistry.getTargetContext().getCacheDir());
            _imageUri = Uri.fromFile(temporaryFile);
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
            imageDataBase.AddImage("TestImage" + i, creationDate.toInstant(ZoneOffset.UTC).toEpochMilli(),
                    _imageUri.toString(), MyApplication.getCurrentLecture());
        }
    }

    @Test
    public void sampleImagesAreVisible() throws Exception {
        onView(withText("TestImage2")).check(matches(isDisplayed()));
        onView(withText("TestImage3")).check(matches(isDisplayed()));
    }

    @Test
    public void sampleSeparatorsAreVisible() throws Exception {
        String firstDateString = DateTimeUtils.FormatDateTimeToMonthAndYear(firstDate);
        String secondDateString = DateTimeUtils.FormatDateTimeToMonthAndYear(secondDate);

        onView(withText(firstDateString)).check(matches(isDisplayed()));
        onView(withText(secondDateString)).check(matches(isDisplayed()));
    }
}
