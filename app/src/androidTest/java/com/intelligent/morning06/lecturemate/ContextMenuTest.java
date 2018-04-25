package com.intelligent.morning06.lecturemate;



import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.ListView;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static java.lang.Boolean.FALSE;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


@RunWith(AndroidJUnit4.class)
public class ContextMenuTest {
    DataBaseAccessLecture dataBase = null;

    @Before
    public void deleteTableIfExists() throws Exception {
        //DataBaseAccessLecture dataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());
        if(dataBase == null)
            dataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dataBase.DeleteAllLectures();
    }


    @Rule
    public ActivityTestRule<LecturesActivity> mActivityRule = new
            ActivityTestRule<>(LecturesActivity.class);


    @Test
    public void DeleteTest() throws Exception {

        add("DeleteTest");


        try {
            onView(withText("DeleteTest")).perform(longClick());

        } catch (Exception NoValueInListException) {
            Assert.fail("Cant find DeleteTest");
        }

        try {
            onView(withId(R.id.Delete)).perform(click());
        } catch (Exception NoValueInListException) {
            Assert.fail("Cant find delete button");
        }

       if (dataBase.GetAllLectures().isEmpty() == FALSE) {
                Assert.fail("Cant delete lecture");
       }



    }

    @Test
    public void EditTest() throws Exception {

        add("EditTest");

        try {
            onView(withText("EditTest")).perform(longClick());

        } catch (Exception NoValueInListException) {
            Assert.fail("Cant find EditTest");
        }

        try {
            onView(withId(R.id.Edit)).perform(click());
        } catch (Exception NoValueInListException) {
            Assert.fail("Cant find edit button");
        }

        try {
            onView(withInputType(InputType.TYPE_CLASS_TEXT)).perform(typeText("InputTest"));

            onView(withText("Edit")).perform(click());

            }
        catch (Exception EditException)
        {
            Assert.fail("Cant edit Lecture");
        }

        if(!dataBase.GetAllLectures().get(0).getLectureName().equals( "EditTestInputTest")){
            Assert.fail("Edit lecture didnt work");
        }


    }

    public void add(String lecture){
        onView(withId(R.id.fab)).perform(click());
        LecturesActivity lecturesActivity = mActivityRule.getActivity();

        AlertDialog addLectureDialog = lecturesActivity.getLastDialog();
        if(addLectureDialog.isShowing()) {
            onView(withInputType(InputType.TYPE_CLASS_TEXT)).perform(typeText(lecture));
            onView(withText("ADD")).perform(click());
            try {
                onView(withText(lecture)).check(matches(isDisplayed()));
            }catch(NoMatchingViewException exception) {
                Assert.fail("Lecture was not correctly added");
            }
        }
        else {
            Assert.fail("Add lecture dialog not showing");
        }
    }

}

