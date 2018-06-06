package com.intelligent.morning06.lecturemate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.intelligent.morning06.lecturemate.DataAccess.Note;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class NotesActivityInstrumentedTest {

    private final String _someLargeText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus et sagittis dui. Etiam id commodo lorem. Ut vel velit nec magna mollis sagittis id et dolor. Vivamus maximus turpis vitae dui porttitor tempus. Sed sagittis elementum efficitur. Cras varius sollicitudin tincidunt. In maximus, massa id aliquet luctus, mi diam lacinia velit, ac bibendum lacus lectus non eros. Etiam congue ligula nisl, vel dictum ipsum posuere at. Praesent luctus eu velit at malesuada.\n" +
            "\n" +
            "Nullam non arcu nec neque fermentum commodo vitae vel nunc. Nunc tincidunt ante quis arcu placerat feugiat. Curabitur tempor, enim ut elementum pretium, dui diam dictum velit, in faucibus dolor est vel ante. Sed nibh lacus, facilisis malesuada suscipit ut, pharetra ac erat. Mauris consequat turpis enim, a pharetra sem varius vitae. Pellentesque non condimentum velit. Nam et velit nec nibh pretium elementum et a lectus. Curabitur congue pretium elit. Vestibulum eros sapien, posuere vel fringilla vitae, mattis quis augue. Vivamus tincidunt porta velit quis dignissim.\n" +
            "\n" +
            "Proin sodales scelerisque dolor eu commodo. Nullam tincidunt leo id arcu finibus, vitae viverra erat facilisis. Sed malesuada lobortis ipsum id vulputate. Phasellus feugiat vulputate mattis. Vestibulum ligula est, bibendum in porttitor non, bibendum vel ex. Sed sem eros, ullamcorper vel tortor nec, auctor condimentum mi. Curabitur eleifend, mi id elementum viverra, tellus purus venenatis mi, quis rhoncus neque nisi a lacus. Cras non elementum ligula. Donec at neque laoreet, hendrerit lorem sit amet, cursus sem. Phasellus rutrum erat ut euismod vulputate. Vivamus et turpis nec ligula mollis fringilla. Nunc tincidunt eget dui ac bibendum. In rhoncus ipsum vitae arcu vehicula porta.\n" +
            "\n" +
            "Duis vitae tortor et velit tempor pellentesque id id dui. Praesent dapibus est at nisi posuere pulvinar. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porta enim vitae tortor pellentesque luctus. Quisque pellentesque quam eget ipsum pellentesque elementum. Donec ultrices ipsum vestibulum nulla varius, eu laoreet magna aliquam. Quisque maximus, nisl et ultrices tempus, massa dui blandit lorem, sed mattis massa erat vel orci. Pellentesque dignissim, diam ac viverra eleifend, tortor metus bibendum urna, non vestibulum diam elit sed ipsum. Suspendisse sit amet lorem quam. Pellentesque sagittis aliquet eleifend. Ut eleifend pulvinar interdum.\n" +
            "\n" +
            "Nullam justo quam, ornare in ligula eget, vehicula ornare dui. Ut ut nisl eget justo auctor hendrerit. Vestibulum volutpat consectetur eros, vitae bibendum erat elementum eget. Nulla egestas eros ac tellus vulputate, id egestas enim sagittis. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nulla dictum placerat dui in hendrerit. Vivamus eu nibh sed libero sodales molestie. Nam porta erat quis ante rutrum finibus. Maecenas at lectus lorem. Quisque quis nisi blandit, imperdiet quam vitae, finibus erat. Maecenas pulvinar blandit varius. Nulla egestas nibh mauris, vel laoreet nulla posuere vel. Quisque aliquam diam vel turpis posuere hendrerit. Vestibulum congue gravida justo, vel suscipit orci commodo at. Etiam ultricies sollicitudin ligula, quis dignissim odio hendrerit laoreet. Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

    public ArrayList<Note> notes;

    @Rule
    public ActivityTestRule<NotesActivity> mActivityRule =
            new ActivityTestRule<NotesActivity>(NotesActivity.class) {
                @Override
                protected Intent getActivityIntent() {

                    Note first = new Note(1, "FirstNote", _someLargeText + "\n\nAAAA\n\n", LocalDateTime.now(), 1);
                    Note second = new Note(1, "SecondNote", _someLargeText + "\n\nBBBB\n\n", LocalDateTime.now(), 1);
                    Note third = new Note(1, "ThirdNote", _someLargeText + "\n\nCCCC\n\n", LocalDateTime.now(), 1);

                    notes = new ArrayList<Note>();
                    notes.add(first);
                    notes.add(second);
                    notes.add(third);

                    int selectedIndex = 0;

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ALL_NOTES", notes);

                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, NotesActivity.class);
                    result.putExtra("SERIALIZED_DATA", bundle);
                    result.putExtra("SELECTED_INDEX", selectedIndex);
                    return result;
                }
            };

    @Test
    public void testCreateActivity() throws Exception {
        onView(withText(notes.get(0).getText())).check(matches(isDisplayed()));
        onView(withText(notes.get(0).getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")))).check(matches(isDisplayed()));
    }

    @Test
    public void testLeftButton() throws Exception {
        onView(withId(R.id.notes_view_action_left)).perform(click());
        onView(withText(notes.get(2).getText())).check(matches(isDisplayed()));
        onView(withText(notes.get(2).getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")))).check(matches(isDisplayed()));

        onView(withId(R.id.notes_view_action_left)).perform(click());
        onView(withText(notes.get(1).getText())).check(matches(isDisplayed()));
        onView(withText(notes.get(1).getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")))).check(matches(isDisplayed()));

        onView(withId(R.id.notes_view_action_left)).perform(click());
        onView(withText(notes.get(0).getText())).check(matches(isDisplayed()));
        onView(withText(notes.get(0).getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")))).check(matches(isDisplayed()));
    }

    @Test
    public void testRightButton() throws Exception {
        onView(withId(R.id.notes_view_action_right)).perform(click());
        onView(withText(notes.get(1).getText())).check(matches(isDisplayed()));
        onView(withText(notes.get(1).getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")))).check(matches(isDisplayed()));

        onView(withId(R.id.notes_view_action_right)).perform(click());
        onView(withText(notes.get(2).getText())).check(matches(isDisplayed()));
        onView(withText(notes.get(2).getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")))).check(matches(isDisplayed()));

        onView(withId(R.id.notes_view_action_right)).perform(click());
        onView(withText(notes.get(0).getText())).check(matches(isDisplayed()));
        onView(withText(notes.get(0).getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")))).check(matches(isDisplayed()));
    }

}
