package com.intelligent.morning06.lecturemate;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessDate;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

import junit.framework.Assert;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class DatesCreateActivityInstrumentedTest {
    private DataBaseAccessDate dateDataBase = null;
    private DataBaseAccessLecture lectureDataBase = null;
    private int lectureId = 0;

    @Rule
    public ActivityTestRule<DatesCreateActivity> mActivityRule =
            new ActivityTestRule<DatesCreateActivity>(DatesCreateActivity.class);

    @Before
    public void setUp() throws Exception {
        if(dateDataBase == null)
            dateDataBase = new DataBaseAccessDate(InstrumentationRegistry.getTargetContext());
        if(lectureDataBase == null)
            lectureDataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dateDataBase.DeleteAllDates();
        lectureDataBase.DeleteAllLectures();

        try {
            Lecture lecture = DataModel.GetInstance().getLectureDataBase().AddLecture("TestLecture");
            lectureId = lecture.getId();
            MyApplication.setCurrentLecture(lectureId);
        } catch(LectureAlreadyExistsException exception) {
            Assert.fail("Lecture already exists. Should not happen");
        }
    }

    @Test
    public void createDate_cancelWorks() throws Exception {
        onView(withId(R.id.dates_create_activity_action_cancel)).perform(click());
        Assert.assertEquals(true, mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void createDate_ToolbarButtonsExist() throws Exception {
        onView(withId(R.id.dates_create_activity_action_save)).perform(click());
        onView(withId(R.id.dates_create_activity_action_cancel)).perform(click());
    }

    @Test
    public void createDate_AddDate_TitleEmpty() throws Exception {
        onView(withId(R.id.dates_create_activity_action_save)).perform(click());
        onView(withText(containsString("Title cannot be empty"))).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void createDate_AddDate_TextEmpty() throws Exception {
        onView(withId(R.id.editTextTitle)).perform(typeText("TestDate"));

        onView(withId(R.id.dates_create_activity_action_save)).perform(click());
        onView(withText(containsString("Date text cannot be empty"))).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void createDate_AddDate_DateEmpty() throws Exception {
        onView(withId(R.id.editTextTitle)).perform(typeText("TestDate"));
        onView(withId(R.id.editTextDate)).perform(typeText("TestDate"));

        onView(withId(R.id.dates_create_activity_action_save)).perform(click());
        onView(withText(containsString("Date must be selected"))).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }
    @Test
    public void createDate_AddDate_TimeEmpty() throws Exception {
        onView(withId(R.id.editTextTitle)).perform(typeText("TestDate"));
        onView(withId(R.id.editTextDate)).perform(typeText("TestDate"));
        Thread.sleep(1000);
        onView(withId(R.id.Date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2018,12,31));
        onView(withId(android.R.id.button1)).perform(click());


        onView(withId(R.id.dates_create_activity_action_save)).perform(click());
        onView(withText(containsString("Time must be selected"))).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }
    @Test
    public void addDate_LectureDoesNotExist_Toast() throws Exception {
        onView(withId(R.id.editTextTitle)).perform(typeText("TestDate"));
        onView(withId(R.id.editTextDate)).perform(typeText("TestDate TestText"));
        Espresso.pressBack();
        Thread.sleep(1000);
        onView(withId(R.id.Time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12,00));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.Date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2018,12,31));
        onView(withId(android.R.id.button1)).perform(click());



        MyApplication.setCurrentLecture(-20);

        onView(withId(R.id.dates_create_activity_action_save)).perform(click());
        onView(withText(containsString("Could not add date to database"))).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void addDate_CorrectlyAdded() throws Exception {
        onView(withId(R.id.editTextTitle)).perform(typeText("TestDate"));
        onView(withId(R.id.editTextDate)).perform(typeText("TestDate TestText"));
        Espresso.pressBack();
        Thread.sleep(1000);
        onView(withId(R.id.Time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12,00));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.Date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2018,12,31));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.dates_create_activity_action_save)).perform(click());

        Cursor cursor = dateDataBase.GetDateCursorForLecture(lectureId);

        Assert.assertEquals(true, cursor.moveToFirst());
        String dateTitle = cursor.getString(cursor.getColumnIndex(DataBaseAccessDate.DateTable.COLUMN_NAME_TITLE));
        Assert.assertEquals("TestDate", dateTitle);
        Assert.assertEquals(true, mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void testSelectedTimeView() throws Exception {
        onView(withId(R.id.Time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(11,12));
        onView(withId(android.R.id.button1)).perform(click());
        Assert.assertEquals("11:12", ((EditText)mActivityRule.getActivity().findViewById(R.id.Time)).getText().toString());

        onView(withId(R.id.Time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(1,12));
        onView(withId(android.R.id.button1)).perform(click());
        Assert.assertEquals("01:12", ((EditText)mActivityRule.getActivity().findViewById(R.id.Time)).getText().toString());

        onView(withId(R.id.Time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(11,2));
        onView(withId(android.R.id.button1)).perform(click());
        Assert.assertEquals("11:02", ((EditText)mActivityRule.getActivity().findViewById(R.id.Time)).getText().toString());

        onView(withId(R.id.Time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(2,2));
        onView(withId(android.R.id.button1)).perform(click());
        Assert.assertEquals("02:02", ((EditText)mActivityRule.getActivity().findViewById(R.id.Time)).getText().toString());
    }
}
