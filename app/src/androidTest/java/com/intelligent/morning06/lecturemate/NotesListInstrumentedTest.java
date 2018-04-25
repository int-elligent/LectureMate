package com.intelligent.morning06.lecturemate;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AlertDialog;
import android.text.InputType;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessNote;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by MitaF on 24. 04. 2018.
 */

@RunWith(AndroidJUnit4.class)
public class NotesListInstrumentedTest {

    DataBaseAccessLecture dataBaseLecture = null;
    DataBaseAccessNote dataBaseNote = null;

    @Rule
    public ActivityTestRule<NotesListActivity> NotesListActivityRule = new
            ActivityTestRule<>(NotesListActivity.class);

    @Before
    public void deleteLecturesIfExist() throws Exception {
        if(dataBaseLecture == null)
            dataBaseLecture = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());
        if(dataBaseNote == null)
            dataBaseNote = new DataBaseAccessNote(InstrumentationRegistry.getTargetContext());

        dataBaseLecture.DeleteAllLectures();
        dataBaseNote.DeleteAllNotes();
    }

    @Test
    public void NotesShownCorrectly() {
        dataBaseNote.AddNote("TestTitle", "TestNote", Instant.now().getEpochSecond(), 1);
        NotesListActivityRule.getActivity().updateNotes();

        onView(withText("TestTitle")).perform(click());
    }
}
