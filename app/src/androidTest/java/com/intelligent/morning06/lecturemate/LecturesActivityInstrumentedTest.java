package com.intelligent.morning06.lecturemate;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AlertDialog;
import android.text.InputType;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class LecturesActivityInstrumentedTest {
    DataBaseAccessLecture dataBase = null;

    @Before
    public void deleteTableIfExists() throws Exception {
        if(dataBase == null)
            dataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dataBase.DeleteAllLectures();
    }

    @Rule
    public IntentsTestRule<LecturesActivity> mActivityRule = new
            IntentsTestRule<>(LecturesActivity.class);

    @Test
    public void testButtonAdd() throws Exception {
        onView(withId(R.id.fab)).perform(click());
    }

    @Test
    public void testAddLectureSuccessful() throws Exception {
        onView(withId(R.id.fab)).perform(click());
        LecturesActivity lecturesActivity = mActivityRule.getActivity();

        AlertDialog addLectureDialog = lecturesActivity.getLastDialog();
        if(addLectureDialog.isShowing()) {
            onView(withInputType(InputType.TYPE_CLASS_TEXT)).perform(typeText("TestLecture"));
            onView(withText("ADD")).perform(click());
            try {
                MyApplication.setStoragePermissionGranted(true);
                onView(withText("TestLecture")).perform(click());
                intended(hasComponent(TabCategoriesActivity.class.getName()));
                Assert.assertEquals("TestLecture", MyApplication.getCurrentLectureName());
                Assert.assertEquals(true, MyApplication.getStoragePermissionGranted());
            }catch(NoMatchingViewException exception) {
                Assert.fail("Lecture was not correctly added");
            }
        }
        else {
            Assert.fail("Add lecture dialog not showing");
        }



    }

    @Test
    public void testAddLectureAlreadyExists() throws Exception {
        dataBase.AddLecture("TestLecture");

        onView(withId(R.id.fab)).perform(click());
        LecturesActivity lecturesActivity = mActivityRule.getActivity();

        AlertDialog addLectureDialog = lecturesActivity.getLastDialog();
        if(addLectureDialog.isShowing()) {
            onView(withInputType(InputType.TYPE_CLASS_TEXT)).perform(typeText("TestLecture"));
            onView(withText("ADD")).perform(click());
            onView(withText("Lecture cannot be added, it already exists")).
                    inRoot(withDecorView(not(is(lecturesActivity.getWindow().getDecorView())))).
                    check(matches(isDisplayed()));
        }
        else {
            Assert.fail("Add lecture dialog not showing");
        }
    }

    @Test
    public void testAddLectureEmptyName() throws Exception {
        dataBase.AddLecture("TestLecture");

        onView(withId(R.id.fab)).perform(click());
        LecturesActivity lecturesActivity = mActivityRule.getActivity();

        AlertDialog addLectureDialog = lecturesActivity.getLastDialog();
        if(addLectureDialog.isShowing()) {
            onView(withText(MyApplication.getContext().getResources().getString(R.string.title_activity_Lectures_ButtonAdd))).perform(click());
            onView(withText(MyApplication.getContext().getResources().getString(R.string.error_activity_lectures_lectureNameEmpty))).
                    inRoot(withDecorView(not(is(lecturesActivity.getWindow().getDecorView())))).
                    check(matches(isDisplayed()));
        }
        else {
            Assert.fail("Add lecture dialog not showing");
        }
    }

    @Test
    public void testDialogButtonAdd() throws Exception {
        onView(withId(R.id.fab)).perform(click());
        onView(withText("ADD")).perform(click());
    }

    @Test
    public void testDialogButtonCancel() throws Exception {
        onView(withId(R.id.fab)).perform(click());
        onView(withText("CANCEL")).perform(click());
    }

    @Test
    public void deleteLecture() throws Exception {
        onView(withId(R.id.fab)).perform(click());
        LecturesActivity lecturesActivity = mActivityRule.getActivity();

        AlertDialog addLectureDialog = lecturesActivity.getLastDialog();
        if(addLectureDialog.isShowing()) {
            onView(withInputType(InputType.TYPE_CLASS_TEXT)).perform(typeText("TestLecture"));
            onView(withText("ADD")).perform(click());
            try {
                MyApplication.setStoragePermissionGranted(true);
                onView(withText("TestLecture")).check(matches(isDisplayed()));
                onView(withText("TestLecture")).perform(longClick());
                onView(withText("delete")).perform(click());
                onView(withText("TestLecture")).check(doesNotExist());
            }catch(NoMatchingViewException exception) {
                Assert.fail("Lecture was not correctly added");
            }
        }
    }

}

