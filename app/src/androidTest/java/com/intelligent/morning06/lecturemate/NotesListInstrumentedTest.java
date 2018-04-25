package com.intelligent.morning06.lecturemate;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AlertDialog;
import android.text.InputType;

import com.intelligent.morning06.lecturemate.DataAccess.CategoriesActivity;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessNote;
import com.intelligent.morning06.lecturemate.DataAccess.NotesListActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    @Rule
    public ActivityTestRule<LecturesActivity> LecturesActivityRule = new
            ActivityTestRule<>(LecturesActivity.class);
    public ActivityTestRule<CategoriesActivity> CategoriesActivityRule = new
            ActivityTestRule<>(CategoriesActivity.class);
    public ActivityTestRule<NotesListActivity> NotesListActivityRule = new
            ActivityTestRule<>(NotesListActivity.class);

    @Before
    public void deleteLecturesIfExist() throws Exception {
        if(dataBaseLecture == null)
            dataBaseLecture = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());

        dataBaseLecture.DeleteAllLectures();

        onView(withId(R.id.fab)).perform(click());
        LecturesActivity lecturesActivity = LecturesActivityRule.getActivity();

        AlertDialog addLectureDialog = lecturesActivity.getLastDialog();
        if (addLectureDialog.isShowing()) {
            onView(withInputType(InputType.TYPE_CLASS_TEXT)).perform(typeText("TestLecture"));
            onView(withText("ADD")).perform(click());
        }

        onView(withText("TestLecture")).perform(click());

    }

    @Test
    public void testNotesActivityOpening() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(NotesListActivity.class.getName(), null, false);

        onView(withText("Notes")).perform(click());

        NotesListActivity notesListActivity= (NotesListActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        assertNotNull(notesListActivity);
        notesListActivity.finish();

    }

    @Test
    public void testCategoriesListStandardCategories() throws InterruptedException {
        //TODO database access of notes
//        dataAccessNote.AddNote("Note 1", "This is Note 1", 123123, 0);
//        dataAccessNote.AddNote("Note 2", "This is Note 2", 31243123, 0);
//        dataAccessNote.AddNote("Note 3", "This is Note 3", 312312, 0);

        onView(withText("Notes")).perform(click());

        onView(withText("Note 1")).perform(click());
        onView(withText("Note 2")).perform(click());
        onView(withText("Note 3")).perform(click());

    }


}
