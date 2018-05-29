package com.intelligent.morning06.lecturemate;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessImage;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseConstants;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Image;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;
import com.intelligent.morning06.lecturemate.DataAccess.Note;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TimeZone;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see​ ​<a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

/*public class ImageViewInstrumentedTest {
    @Rule
    public ActivityTestRule<ImageViewActivity> mActivityRule =
            new ActivityTestRule<ImageViewActivity>(ImageViewActivity.class) {
                @Override
                protected Intent getActivityIntent() {

                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, ImageViewActivity.class);
                    return result;
                }
            };
    @Test
    public void testCreateActivity() throws Exception {
        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.viewPager)).perform(swipeLeft());
    }
}*/
@RunWith(AndroidJUnit4.class)
public class ImageViewInstrumentedTest {
    private DataBaseAccessImage imageDataBase = null;
    private DataBaseAccessLecture lectureDataBase = null;
    private int lectureId = 0;
   private ArrayList<Image> images_;
    private LocalDateTime firstDate;
    private LocalDateTime secondDate;

    @Rule
    public ActivityTestRule<ImageViewActivity> m1ActivityRule =
            new ActivityTestRule<ImageViewActivity>(ImageViewActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    try {
                      setUp();
                    }catch(Exception exception) {
                        Log.d("test", exception.getMessage());
                    }

                    Bundle bundle  = new Bundle();
                    bundle.putSerializable("ALL_IMAGES", images_);
                    Context targetContext = getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, ImageViewActivity.class);
                    result.putExtra("SERIALIZED_DATA", bundle);
                    result.putExtra("SELECTED_INDEX", 0);
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

        Cursor imageCursor = DataModel.GetInstance().getImageDataBase().
                GetImageCursorForLecture(MyApplication.getCurrentLecture());

        images_ = new ArrayList<Image>();

        while(imageCursor.moveToNext()) {
            String title = imageCursor.
                    getString(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_TITLE));
            String filePath = imageCursor.
                    getString(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_FILEPATH));
            int id = imageCursor.
                    getInt(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_ID));
            LocalDateTime creationDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(
                    imageCursor.getLong(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_CREATIONDATE))),
                    TimeZone.getDefault().toZoneId());

            Image image = new Image(id, title, filePath, creationDate, MyApplication.getCurrentLecture());

            images_.add(image);
        }
    }

    private void insertSampleImages() {
        firstDate = LocalDateTime.of(2010, 12, 10, 5, 32);
        secondDate = LocalDateTime.of(2010, 10, 5, 5, 22);
        String temporaryFilePath,temporaryFilePath1,temporaryFilePath2,temporaryFilePath3;

        Bitmap testImage = BitmapFactory.decodeResource(InstrumentationRegistry.getTargetContext().getResources(), R.mipmap.app_icon);
        try {
            File temporaryFile = File.createTempFile("testImage", "tmp", InstrumentationRegistry.getTargetContext().getCacheDir());

            OutputStream fileStream = new FileOutputStream(temporaryFile);
            testImage.compress(Bitmap.CompressFormat.JPEG, 10, fileStream);
            temporaryFilePath = temporaryFile.getAbsolutePath();
        } catch (IOException ioException) {
            Assert.fail("Could not create temporary image for testing purposes: " + ioException.getMessage());
            return;
        }
        Bitmap testImage1 = BitmapFactory.decodeResource(InstrumentationRegistry.getTargetContext().getResources(), R.drawable.image1);
        try {
            File temporaryFile = File.createTempFile("testImage", "tmp", InstrumentationRegistry.getTargetContext().getCacheDir());

            OutputStream fileStream = new FileOutputStream(temporaryFile);
            testImage1.compress(Bitmap.CompressFormat.JPEG, 10, fileStream);
            temporaryFilePath1 = temporaryFile.getAbsolutePath();
        } catch (IOException ioException) {
            Assert.fail("Could not create temporary image for testing purposes: " + ioException.getMessage());
            return;
        }
        Bitmap testImage2 = BitmapFactory.decodeResource(InstrumentationRegistry.getTargetContext().getResources(), R.drawable.image2);
        try {
            File temporaryFile = File.createTempFile("testImage", "tmp", InstrumentationRegistry.getTargetContext().getCacheDir());

            OutputStream fileStream = new FileOutputStream(temporaryFile);
            testImage2.compress(Bitmap.CompressFormat.JPEG, 10, fileStream);
            temporaryFilePath2 = temporaryFile.getAbsolutePath();
        } catch (IOException ioException) {
            Assert.fail("Could not create temporary image for testing purposes: " + ioException.getMessage());
            return;
        }
        Bitmap testImage3 = BitmapFactory.decodeResource(InstrumentationRegistry.getTargetContext().getResources(), R.drawable.image3);
        try {
            File temporaryFile = File.createTempFile("testImage", "tmp", InstrumentationRegistry.getTargetContext().getCacheDir());

            OutputStream fileStream = new FileOutputStream(temporaryFile);
            testImage3.compress(Bitmap.CompressFormat.JPEG, 10, fileStream);
            temporaryFilePath3 = temporaryFile.getAbsolutePath();
        } catch (IOException ioException) {
            Assert.fail("Could not create temporary image for testing purposes: " + ioException.getMessage());
            return;
        }

        int lectureId = MyApplication.getCurrentLecture();
        imageDataBase.AddImage("TestImage", firstDate.toInstant(ZoneOffset.UTC).toEpochMilli(), temporaryFilePath, MyApplication.getCurrentLecture());
        imageDataBase.AddImage("TestImage1", secondDate.toInstant(ZoneOffset.UTC).toEpochMilli(), temporaryFilePath1, MyApplication.getCurrentLecture());
        imageDataBase.AddImage("TestImage2", firstDate.toInstant(ZoneOffset.UTC).toEpochMilli(), temporaryFilePath2, MyApplication.getCurrentLecture());
        imageDataBase.AddImage("TestImage3", firstDate.toInstant(ZoneOffset.UTC).toEpochMilli(), temporaryFilePath3, MyApplication.getCurrentLecture());


}
    @Test
    public void indexCalculatedCorrectly() throws Exception {

    onView(withId(R.id.viewPager)).perform(swipeLeft());
    onView(withId(R.id.viewPager)).perform(swipeLeft());
    assertEquals(m1ActivityRule.getActivity().viewPager.getCurrentItem(), 2);
    onView(withId(R.id.viewPager)).perform(swipeLeft());
    onView(withId(R.id.viewPager)).perform(swipeLeft());
    onView(withId(R.id.viewPager)).perform(swipeLeft());
    assertEquals(m1ActivityRule.getActivity().viewPager.getCurrentItem(), 3);
}
    @Test
    public void correctImageDisplayed() throws Exception {
        Bitmap testImage2 = BitmapFactory.decodeResource(InstrumentationRegistry.getTargetContext().getResources(), R.drawable.image2);
        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.viewPager)).perform(swipeLeft());
        assertFalse(m1ActivityRule.getActivity().imageViewPagerAdapter.imgBitmap.sameAs(testImage2));

    }
}

