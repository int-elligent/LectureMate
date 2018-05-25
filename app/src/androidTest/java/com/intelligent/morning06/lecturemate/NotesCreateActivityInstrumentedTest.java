package com.intelligent.morning06.lecturemate;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessNote;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class NotesCreateActivityInstrumentedTest {
    private DataBaseAccessNote noteDataBase = null;
    private DataBaseAccessLecture lectureDataBase = null;
    private int lectureId = 0;

    @Rule
    public ActivityTestRule<NotesCreateActivity> mActivityRule =
            new ActivityTestRule<NotesCreateActivity>(NotesCreateActivity.class);

    @Before
    public void setUp() throws Exception {
        if(noteDataBase == null)
            noteDataBase = new DataBaseAccessNote(InstrumentationRegistry.getTargetContext());
        if(lectureDataBase == null)
            lectureDataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        noteDataBase.DeleteAllNotes();
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
    public void createNode_cancelWorks() throws Exception {
        onView(withId(R.id.notes_create_activity_action_cancel)).perform(click());
        Assert.assertEquals(true, mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void createNote_ToolbarButtonsExist() throws Exception {
        onView(withId(R.id.notes_create_activity_action_save)).perform(click());
        onView(withId(R.id.notes_create_activity_action_cancel)).perform(click());
    }

    @Test
    public void createNote_AddNote_TitleEmpty() throws Exception {
        onView(withId(R.id.notes_create_activity_action_save)).perform(click());
        onView(withText(containsString("Title cannot be empty"))).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void createNote_AddNote_TextEmpty() throws Exception {
        onView(withId(R.id.editTextTitle)).perform(typeText("TestNote"));

        onView(withId(R.id.notes_create_activity_action_save)).perform(click());
        onView(withText(containsString("Note text cannot be empty"))).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void addNote_LectureDoesNotExist_Toast() throws Exception {
        onView(withId(R.id.editTextTitle)).perform(typeText("TestNote"));
        onView(withId(R.id.editTextNote)).perform(typeText("TestNote TestText"));
        MyApplication.setCurrentLecture(-20);

        onView(withId(R.id.notes_create_activity_action_save)).perform(click());
        onView(withText(containsString("Database Error:"))).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void addNote_CorrectlyAdded() throws Exception {
        onView(withId(R.id.editTextTitle)).perform(typeText("TestNote"));
        onView(withId(R.id.editTextNote)).perform(typeText("TestNote TestText"));

        onView(withId(R.id.notes_create_activity_action_save)).perform(click());

        Cursor cursor = noteDataBase.GetNoteCursorForLecture(MyApplication.getCurrentLecture());

        Assert.assertEquals(true, cursor.moveToFirst());
        String noteTitle = cursor.getString(cursor.getColumnIndex(DataBaseAccessNote.NoteTable.COLUMN_NAME_TITLE));
        Assert.assertEquals("TestNote", noteTitle);
        Assert.assertEquals(true, mActivityRule.getActivity().isFinishing());
    }
}
