package com.intelligent.morning06.lecturemate;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessLecture;
import com.intelligent.morning06.lecturemate.ListFragments.ImagesListFragment;
import com.intelligent.morning06.lecturemate.ListFragments.NotesListFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
public class TabCategoriesInstrumentedTest {

    DataBaseAccessLecture dataBase = null;

    @Rule
    public ActivityTestRule<TabCategoriesActivity> mActivityRule = new
            ActivityTestRule<TabCategoriesActivity>(TabCategoriesActivity.class, true, false);

    @Rule
    public GrantPermissionRule mRuntimePermissionRuleReadStorage = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule mRuntimePermissionRuleWriteStorage = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);


    @Before
    public void deleteLecturesIfExist() throws Exception {
        if (dataBase == null) {
            dataBase = new DataBaseAccessLecture(InstrumentationRegistry.getTargetContext());
        }

        dataBase.DeleteAllLectures();
        dataBase.AddLecture("TestLecture");

        MyApplication.setStoragePermissionGranted(true);
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void testImagesListVisible() {
        onView(withText("Images")).perform(click());
        onView(withId(R.id.images_list_listview)).check(matches(isDisplayed()));
    }

    @Test
    public void testNotesListVisible() {
        onView(withText("Notes")).perform(click());
        onView(withId(R.id.notes_list_listview)).check(matches(isDisplayed()));
    }

    @Test
    public void testDatesListVisible() {
        onView(withText("Dates")).perform(click());
        onView(withId(R.id.dates_list_listview)).check(matches(isDisplayed()));
    }
}
