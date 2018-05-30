package com.intelligent.morning06.lecturemate;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessDate;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by MitaF on 24. 04. 2018.
 */

    @RunWith(AndroidJUnit4.class)
    public class DateListInstrumentedTest {
        private DataBaseAccessDate dateDataBase = null;
        private DataBaseAccessLecture lectureDataBase = null;
        private int lectureId = 0;

        private LocalDateTime firstDate;
        private LocalDateTime secondDate;

        @Rule
        public ActivityTestRule<DatesListActivity> mActivityRule =
                new ActivityTestRule<DatesListActivity>(DatesListActivity.class) {
                    @Override
                    protected Intent getActivityIntent() {
                        try {
                            setUp();
                        } catch (Exception exception) {
                            Log.d("test", exception.getMessage());
                        }


                        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                        Intent result = new Intent(targetContext, DatesListActivity.class);
                        return result;
                    }
                };

        public void setUp() throws Exception {
            if (dateDataBase == null)
                dateDataBase = new DataBaseAccessDate(InstrumentationRegistry.getTargetContext());
            if (lectureDataBase == null)
                lectureDataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

            dateDataBase.DeleteAllDates();
            lectureDataBase.DeleteAllLectures();

            try {
                Lecture lecture = DataModel.GetInstance().getLectureDataBase().AddLecture("TestLecture");
                lectureId = lecture.getId();
                MyApplication.setCurrentLecture(lectureId);
            } catch (LectureAlreadyExistsException exception) {
                Assert.fail("Lecture already exists. Should not happen");
            }

            insertSampleDates();
        }

        private void insertSampleDates() {

            for (int i = 0; i < 4; i++) {


                int lectureId = MyApplication.getCurrentLecture();
                dateDataBase.AddDate("TestDate" + i, "TEXT" + i, Instant.now().getEpochSecond(), Instant.now().getEpochSecond(), MyApplication.getCurrentLecture());
            }
        }

        @Test
        public void sampleDatesAreVisible() throws Exception {
            onView(withText("TestDate2")).perform(click());
            onView(withText("TestDate3")).perform(click());
        }



}
