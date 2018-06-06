package com.intelligent.morning06.lecturemate;


import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessDate;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.MyDate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.TimeZone;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringContains.containsString;


@RunWith(AndroidJUnit4.class)
public class DatesListFragmentInstrumentedTest {

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
    public IntentsTestRule<TabCategoriesActivity> mActivityRule = new
            IntentsTestRule<TabCategoriesActivity>(TabCategoriesActivity.class, true, false);

    @Before
    public void navigateToDateListFragment() throws Exception {
        setUp();
        MyApplication.setStoragePermissionGranted(true);
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
        onView(withText("Dates")).perform(click());
    }


    public void setUp() throws Exception {
        if (dataBaseDate == null)
            dataBaseDate = new DataBaseAccessDate(InstrumentationRegistry.getTargetContext());
        if (dataBaseLecture == null)
            dataBaseLecture = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dataBaseDate.DeleteAllDates();
        dataBaseLecture.DeleteAllLectures();

        dataBaseLecture.AddLecture("TestingDatesList1");
        int id = dataBaseLecture.GetAllLectures().get(0).getId();
        LocalDateTime ldt = LocalDateTime.now(TimeZone.getDefault().toZoneId());
        MyApplication.setCurrentLecture(id);
        MyApplication.setCurrentLectureName("TestingDatesList1");

        MyDate date1 = new MyDate(titles[0], desc[0], ldt, id, ldt.minusDays(10));
        MyDate date2 = new MyDate(titles[1], desc[1], ldt, id, ldt.minusDays(1));
        MyDate date3 = new MyDate(titles[2], desc[2], ldt, id, ldt);
        MyDate date4 = new MyDate(titles[3], desc[3], ldt, id, ldt.plusDays(1));
        MyDate date5 = new MyDate(titles[4], desc[4], ldt, id, ldt.plusDays(10));

        dataBaseDate.AddDate(date1);
        dataBaseDate.AddDate(date2);
        dataBaseDate.AddDate(date3);
        dataBaseDate.AddDate(date4);
        dataBaseDate.AddDate(date5);
    }

    @Test
    public void testDatesDisplayed() throws InterruptedException{
        for(int i = 0; i < 5; i++){
            onView(withText(titles[i])).check(matches(isDisplayed()));
        }
    }

    @Test
    public void createDateClicked() throws Exception {
        onView(withId(R.id.fab_categories_activity)).perform(click());
        intended(hasComponent(DatesCreateActivity.class.getName()));
        onView(withId(R.id.dates_create_activity_action_cancel)).perform(click());
    }

    @Test
    public void testDateOpen() throws Exception {
        onView(withText(titles[0])).perform(click());
        intended(hasComponent(DatesActivity.class.getName()));
    }


}
