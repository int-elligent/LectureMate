package com.intelligent.morning06.lecturemate;

import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessNote;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class NotesCreateActivity extends AppCompatActivity {

    private int _lectureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_create);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_notes_toolbar);
        toolbar.inflateMenu(R.menu.menu_notes_create_activity);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.notes_create_activity_action_save) {
                    SaveNote();
                } else if (menuItem.getItemId() == R.id.notes_create_activity_action_cancel) {
                    finish();
                }
                return true;
            }
        });


    }

    private void SaveNote() {
        String title = ((EditText)findViewById(R.id.editTextTitle)).getText().toString();
        String text = ((EditText)findViewById(R.id.editTextNote)).getText().toString();

        if(title.isEmpty()) {
            ShowToast("Title cannot be empty");
            return;
        }

        if (text.isEmpty()) {
            ShowToast("Note text cannot be empty");
            return;
        }

        try {
            DataModel.GetInstance().getNoteDataBase().AddNote(title, text, Instant.now().toEpochMilli(), MyApplication.getCurrentLecture());
        } catch(SQLException exception) {
            ShowToast("Could not add note to database: " + exception.getMessage());
            return;
        }

        finish();
    }

    private void ShowToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}
