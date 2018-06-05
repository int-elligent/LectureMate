package com.intelligent.morning06.lecturemate;


import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessNote;
import com.intelligent.morning06.lecturemate.DataAccess.Note;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimeZone;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class NotesListFragmentInstrumentedTest {

    DataBaseAccessLecture dataBaseLecture = null;
    DataBaseAccessNote dataBaseNote = null;

    private ArrayList<Note> _allNotes;

    @Rule
    public IntentsTestRule<TabCategoriesActivity> mActivityRule = new
            IntentsTestRule<TabCategoriesActivity>(TabCategoriesActivity.class, true, false);

    @Before
    public void navigateToNoteListFragment() throws Exception {
        setUp();
        MyApplication.setStoragePermissionGranted(true);
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
        onView(withText("Notes")).perform(click());
    }


    public void setUp() throws Exception {
        if (dataBaseNote == null)
            dataBaseNote = new DataBaseAccessNote(InstrumentationRegistry.getTargetContext());
        if (dataBaseLecture == null)
            dataBaseLecture = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dataBaseNote.DeleteAllNotes();
        dataBaseLecture.DeleteAllLectures();
        _allNotes = new ArrayList<Note>();

        dataBaseLecture.AddLecture("TestingNotesList1");
        int id = dataBaseLecture.GetAllLectures().get(0).getId();
        LocalDateTime ldt = LocalDateTime.now(TimeZone.getDefault().toZoneId());
        MyApplication.setCurrentLecture(id);
        MyApplication.setCurrentLectureName("TestingNotesList1");

        dataBaseNote.AddNote("TestNoteForTestLecture1_1", "TestText", System.currentTimeMillis(), id);
        dataBaseNote.AddNote("TestNoteForTestLecture1_2", "TestText1", System.currentTimeMillis(), id);
        dataBaseNote.AddNote("TestNoteForTestLecture2_1", "TestText2", System.currentTimeMillis(), id);
        dataBaseNote.AddNote("TestNoteForTestLecture2_2", "TestText3", System.currentTimeMillis(), id);
        dataBaseNote.AddNote("TestNoteForTestLecture2_3", "TestText4", System.currentTimeMillis(), id);
    }

    @Test
    public void testNotesDisplayed() throws Exception{
        onView(withText("TestNoteForTestLecture1_1")).check(matches(isDisplayed()));

        onView(withText("TestNoteForTestLecture2_3")).check(matches(isDisplayed()));

        onView(withText("TestText")).check(matches(isDisplayed()));
    }

    @Test
    public void testNoteOpen() throws Exception {
        onView(withText("TestNoteForTestLecture1_1")).perform(click());
        intended(hasComponent(NotesActivity.class.getName()));
    }

    @Test
    public void createNoteClicked() throws Exception {
        onView(withId(R.id.fab_categories_activity)).perform(click());
        intended(hasComponent(NotesCreateActivity.class.getName()));
        onView(withId(R.id.notes_create_activity_action_cancel)).perform(click());
    }

}
