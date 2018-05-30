package com.intelligent.morning06.lecturemate;


import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.TextView;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessDate;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.Dates;

import junit.framework.Assert;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
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

    @Rule
    public ActivityTestRule<DatesListActivity> DatesListActivityRule = new
            ActivityTestRule<>(DatesListActivity.class);

    @Before
    public void resetLectures() throws Exception {
        if(dataBaseLecture == null)
            dataBaseLecture = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());
        if(dataBaseDate == null)
            dataBaseDate = new DataBaseAccessDate(InstrumentationRegistry.getTargetContext());

        dataBaseLecture.DeleteAllLectures();
        dataBaseDate.DeleteAllDates();
        dataBaseLecture.AddLecture("TestingDatesList1");

        int id = dataBaseLecture.GetAllLectures().get(0).getId();
        MyApplication.setCurrentLecture(id);

        LocalDateTime ldt = LocalDateTime.now(TimeZone.getDefault().toZoneId());

        Dates date1 = new Dates(titles[0], desc[0], ldt, id, ldt.minusDays(10));
        Dates date2 = new Dates(titles[1], desc[1], ldt, id, ldt.minusDays(1));
        Dates date3 = new Dates(titles[2], desc[2], ldt, id, ldt);
        Dates date4 = new Dates(titles[3], desc[3], ldt, id, ldt.plusDays(1));
        Dates date5 = new Dates(titles[4], desc[4], ldt, id, ldt.plusDays(10));

        dataBaseDate.AddDate(date1);
        dataBaseDate.AddDate(date2);
        dataBaseDate.AddDate(date3);
        dataBaseDate.AddDate(date4);
        dataBaseDate.AddDate(date5);

        /*
        long mpd = 86400000;
        dataBaseDate.AddDate("A1Dead",
                "Assignment 1 deadline",
                Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli() - 10*mpd,
                id);
        dataBaseDate.AddDate("A2Dead",
                "Assignment 2 deadline",
                Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli() - 1*mpd,
                id);
        dataBaseDate.AddDate("A2Pre",
                "Assignment 2 presentation",
                Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli(),
                id);
        dataBaseDate.AddDate("A3Dead",
                "Assignment 3 deadline",
                Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli() + mpd,
                id);
        dataBaseDate.AddDate("Final",
                "Final exam",
                Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli() + 10*mpd,
                id);
*/

        DatesListActivityRule.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                DatesListActivityRule.getActivity().updateDates();
            }
        });

    }


    @Test
    public void AddedDatesExist() throws InterruptedException{
        for(int i = 0; i < 5; i++){
            onView(withText(titles[i])).check(matches(isDisplayed()));
        }
    }

    @Test
    public void TitlesDisplayed() throws InterruptedException {
        onView(withText(titles[0])).perform(click());
        for(int i = 0; i < 5; i++){
            onView(withText(titles[i])).check(matches(isDisplayed()));
            onView(withId(R.id.action_left)).perform(click());
        }
        onView(withText(titles[titles.length - 1])).check(matches(isDisplayed()));
        for(int i = 4; i >= 0; i--){
            onView(withText(titles[i])).check(matches(isDisplayed()));
            onView(withId(R.id.action_right)).perform(click());
        }
    }

    @Test
    public void RelativeTimeDisplayed() throws InterruptedException {
        for(int i = 0; i < 5; i++){
            onView(withText(titles[i])).perform(click());
            onView(withId(R.id.relativeDateTextView)).check(matches(withText(containsString(relative_times[i]))));
            pressBack();
        }
    }




}
