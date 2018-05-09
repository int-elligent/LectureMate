package com.intelligent.morning06.lecturemate;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
    public ActivityTestRule<LecturesActivity> mActivityRule = new
            ActivityTestRule<>(LecturesActivity.class);

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
                onView(withText("TestLecture")).check(matches(isDisplayed()));
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
            onView(withText("ADD")).perform(click());
            onView(withText("Lecture name must not be empty.")).
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

}

