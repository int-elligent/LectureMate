package com.intelligent.morning06.lecturemate;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.intelligent.morning06.lecturemate.DataAccess.Note;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private ArrayList<Note> _notes;
    private int _selectedIndex;
    private Toolbar _toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.notes_toolbar);
        toolbar.inflateMenu(R.menu.menu_notes_view);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.notes_view_action_left) {
                    switchNote(true);
                } else if (menuItem.getItemId() == R.id.notes_view_action_right) {
                    switchNote(false);
                }
                return true;
            }
        });

        Bundle allNotes = getIntent().getBundleExtra("SERIALIZED_DATA");
        _notes = (ArrayList<Note>) allNotes.getSerializable("ALL_NOTES");
        _selectedIndex = getIntent().getIntExtra("SELECTED_INDEX", 0);

        UpdateContent();
    }

    private void UpdateContent()
    {
        if(_selectedIndex < 0 || _selectedIndex > (_notes.size() - 1))
            return;

        TextView textContent = (TextView) findViewById(R.id.notes_view_content);
        textContent.setText(_notes.get(_selectedIndex).getText());

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(_notes.get(_selectedIndex).getTitle());

        TextView subtitle = (TextView) findViewById(R.id.notes_view_subtitle);
        subtitle.setText(_notes.get(_selectedIndex).getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:m")));
    }

    private void switchNote(boolean left) {
        if(left) {
            if(_selectedIndex == 0)
                _selectedIndex = _notes.size() - 1;
            else
                _selectedIndex--;
        }
        else {
            if(_selectedIndex == _notes.size() - 1)
                _selectedIndex = 0;
            else
                _selectedIndex++;
        }

        UpdateContent();
    }
}
