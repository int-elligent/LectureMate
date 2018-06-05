package com.intelligent.morning06.lecturemate;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.TextView;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessDate;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.MyDate;

import junit.framework.Assert;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringContains.containsString;


@RunWith(AndroidJUnit4.class)
public class DatesActivityInstrumentedTest {

    DataBaseAccessLecture dataBaseLecture = null;
    DataBaseAccessDate dataBaseDate = null;
    private String[] titles = {"A1Dead", "A2Dead", "A2Pre", "A3Dead", "Final"};
    private String[] desc = {"Assignment 1 deadline",
            "Assignment 2 deadline",
            "Assignment 2 presentation",
            "Assignment 3 deadline",
            "Final exam"};
    private String[] relative_times = {"10 days ago", "Yesterday", "Today", "Tomorrow", "10 days left"};
    ArrayList<MyDate> _allDates;

    @Rule
    public ActivityTestRule<DatesActivity> mActivityRule =
            new ActivityTestRule<DatesActivity>(DatesActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    try {
                        setUp();
                    }catch(Exception exception) {
                        Assert.fail(exception.getMessage());
                    }

                    int selectedIndex = 0;

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ALL_DATES", _allDates);

                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, DatesActivity.class);
                    result.putExtra("SERIALIZED_DATA", bundle);
                    result.putExtra("SELECTED_INDEX", selectedIndex);
                    return result;
                }
            };

    @Before
    public void setUp() throws Exception {
        if(dataBaseLecture == null)
            dataBaseLecture = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());
        if(dataBaseDate == null)
            dataBaseDate = new DataBaseAccessDate(InstrumentationRegistry.getTargetContext());

        dataBaseLecture.DeleteAllLectures();
        dataBaseDate.DeleteAllDates();
        dataBaseLecture.AddLecture("TestingDatesList1");
        _allDates = new ArrayList<MyDate>();

        int id = dataBaseLecture.GetAllLectures().get(0).getId();
        MyApplication.setCurrentLecture(id);

        LocalDateTime ldt = LocalDateTime.now(TimeZone.getDefault().toZoneId());

        MyDate date1 = new MyDate(titles[0], desc[0], ldt, id, ldt.minusDays(10));
        MyDate date2 = new MyDate(titles[1], desc[1], ldt, id, ldt.minusDays(1));
        MyDate date3 = new MyDate(titles[2], desc[2], ldt, id, ldt);

        dataBaseDate.AddDate(date1);
        dataBaseDate.AddDate(date2);
        dataBaseDate.AddDate(date3);

        _allDates.add(date1);
        _allDates.add(date2);
        _allDates.add(date3);

    }


    @Test
    public void dateIsDisplayed() throws InterruptedException{
        onView(withText(titles[0])).check(matches(isDisplayed()));
        onView(withText(desc[0])).check(matches(isDisplayed()));
    }

    @Test
    public void RelativeTimeDisplayed() throws InterruptedException {
        onView(withId(R.id.relativeDateTextView)).check(matches(withText(containsString(relative_times[0]))));
    }

    @Test
    public void testLeftButton() throws Exception {
        onView(withId(R.id.action_left)).perform(click());
        onView(withText(titles[2])).check(matches(isDisplayed()));
        onView(withText(desc[2])).check(matches(isDisplayed()));

        onView(withId(R.id.action_left)).perform(click());
        onView(withText(titles[1])).check(matches(isDisplayed()));
        onView(withText(desc[1])).check(matches(isDisplayed()));

        onView(withId(R.id.action_left)).perform(click());
        onView(withText(titles[0])).check(matches(isDisplayed()));
        onView(withText(desc[0])).check(matches(isDisplayed()));
    }

    @Test
    public void testRightButton() throws Exception {
        onView(withId(R.id.action_right)).perform(click());
        onView(withText(titles[1])).check(matches(isDisplayed()));
        onView(withText(desc[1])).check(matches(isDisplayed()));

        onView(withId(R.id.action_right)).perform(click());
        onView(withText(titles[2])).check(matches(isDisplayed()));
        onView(withText(desc[2])).check(matches(isDisplayed()));

        onView(withId(R.id.action_right)).perform(click());
        onView(withText(titles[0])).check(matches(isDisplayed()));
        onView(withText(desc[0])).check(matches(isDisplayed()));
    }

}
