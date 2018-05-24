package com.intelligent.morning06.lecturemate;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AlertDialog;
import android.text.InputType;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;

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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LectureCategoriesInstrumentedTest {

    DataBaseAccessLecture dataBase = null;

    @Rule
    public ActivityTestRule<CategoriesActivity> CategoriesActivityRule = new
            ActivityTestRule<>(CategoriesActivity.class);

    @Before
    public void deleteLecturesIfExist() throws Exception {
        if (dataBase == null) {
            dataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());
        }

        dataBase.DeleteAllLectures();
        dataBase.AddLecture("TestLecture");
    }

    @Test
    public void testNotesActivityOpening(){
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(NotesListActivity.class.getName(), null, false);

        onView(withText("Notes")).perform(click());

        NotesListActivity notesListActivity = (NotesListActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        assertNotNull(notesListActivity);
    }

    @Test
    public void testImagesActivityOpening() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ImagesListActivity.class.getName(), null, false);

        onView(withText("Images")).perform(click());

        ImagesListActivity imagesListActivity = (ImagesListActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        assertNotNull(imagesListActivity);
        imagesListActivity.finish();

    }
}
