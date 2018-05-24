package com.intelligent.morning06.lecturemate;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessImage;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessNote;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ImagesCreateActivityInstrumentedTest {
    private DataBaseAccessImage imageDataBase = null;
    private DataBaseAccessLecture lectureDataBase = null;
    private int lectureId = 0;

    @Rule
    public ActivityTestRule<ImagesCreateActivity> mActivityRule =
            new ActivityTestRule<ImagesCreateActivity>(ImagesCreateActivity.class);

    @Before
    public void setUp() throws Exception {
        if(imageDataBase == null)
            imageDataBase = new DataBaseAccessImage(InstrumentationRegistry.getTargetContext());
        if(lectureDataBase == null)
            lectureDataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        imageDataBase.DeleteAllImages();
        lectureDataBase.DeleteAllLectures();

        try {
            Lecture lecture = DataModel.GetInstance().getLectureDataBase().AddLecture("TestLecture12");
            lectureId = lecture.getId();
            MyApplication.setCurrentLecture(lectureId);
        } catch(LectureAlreadyExistsException exception) {
            Assert.fail("Lecture already exists. Should not happen");
        }
    }

    @Test
    public void createImage_cancelWorks() throws Exception {
        onView(withId(R.id.images_create_activity_action_cancel)).perform(click());
        Assert.assertEquals(true, mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void createImage_ToolbarButtonsExist() throws Exception {
        onView(withId(R.id.images_create_activity_action_save)).perform(click());
        onView(withId(R.id.images_create_activity_action_cancel)).perform(click());
    }

    @Test
    public void createImage_AddImage_TextEmpty() throws Exception {

        onView(withId(R.id.images_create_activity_action_save)).perform(click());
        onView(withText(containsString("Image text cannot be empty"))).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void addImage_LectureDoesNotExist_Toast() throws Exception {
        onView(withId(R.id.editTextImage)).perform(typeText("TestImage TestText"));
        MyApplication.setCurrentLecture(-20);

        onView(withId(R.id.images_create_activity_action_save)).perform(click());
        onView(withText(containsString("Could not add image to database"))).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void addNote_CorrectlyAdded() throws Exception {
        onView(withId(R.id.editTextImage)).perform(typeText("TestImage TestText"));

        onView(withId(R.id.images_create_activity_action_save)).perform(click());

        Cursor cursor = imageDataBase.GetImageCursorForLecture(MyApplication.getCurrentLecture());

        Assert.assertEquals(true, cursor.moveToFirst());
        String imageTitle = cursor.getString(cursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_TITLE));
        Assert.assertEquals("TestImage TestText", imageTitle);
        Assert.assertEquals(true, mActivityRule.getActivity().isFinishing());
    }
}
