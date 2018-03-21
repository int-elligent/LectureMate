package com.intelligent.morning06.lecturemate;

/**
 * Created by MitaF on 21. 03. 2018.
 */

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see​ ​<a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LectureAddingInstrumentedTest {
    @Rule
    public ActivityTestRule<LecturesActivity> mActivityRule = new
            ActivityTestRule<>(LecturesActivity.class);

    @Test
    public void testButtonAdd() throws Exception {
        onView(withId(R.id.fab)).perform(click());
    }

    @Test
    public void testDialogButtonAdd() throws Exception {
        onView(withId(R.id.fab)).perform(click());
        onView(withText("ADD")).perform(click());
    }

    @Test
    public void testDialogButtonCancel() throws Exception {
        onView(withId(R.id.fab)).perform(click());
        onView(withText("Cancel")).perform(click());
    }

    @Test
    public void testListView() throws Exception {
        //TODO when database implementation is there
    }

}

