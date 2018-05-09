package com.intelligent.morning06.lecturemate;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessNote;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Note;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimeZone;

public class NotesListActivity extends AppCompatActivity {

    private ListView notesListView;
    public String[] names;
    public String[] previews;
    public String[] creation_dates;
    private String lectureNameToShow;
    private ArrayList<Note> _notes = null;
    private ArrayAdapter<Note> _listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        Intent intent = getIntent();
        lectureNameToShow = intent.getStringExtra(getResources().getString(R.string.intent_extra_lectureName));
        setTitle(lectureNameToShow + " - Notes");

        notesListView = (ListView)findViewById(R.id.notesListView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_note_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateNoteActivity();
            }
        });

        updateNotes();

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openNote(i);
            }
        });

    }

    public int getNumberOfNames(){
        return names.length;
    }

    public void openCreateNoteActivity()
    {
        Intent intent = new Intent(this, NotesCreateActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateNotes();
    }

    public void openNote(int selectedIndex) {
        Intent intent = new Intent(NotesListActivity.this, NotesActivity.class);
        Bundle notesBundle = new Bundle();
        notesBundle.putSerializable(getResources().getString(R.string.intent_extra_allNotes), _notes);
        intent.putExtra(getResources().getString(R.string.intent_extra_serializedData), notesBundle);
        intent.putExtra(getResources().getString(R.string.intent_extra_selectedIndex), selectedIndex);
        startActivity(intent);
    }

    public void updateNotes() {
        int lectureId = MyApplication.getCurrentLecture();
        if(_notes != null)
            _notes.clear();
        _notes = new ArrayList<Note>();
        Cursor notesCursor = DataModel.GetInstance().getNoteDataBase().GetNoteCursorForLecture(lectureId);

        while(notesCursor.moveToNext())
        {
            String title = notesCursor.getString(notesCursor.getColumnIndex(DataBaseAccessNote.NoteTable.COLUMN_NAME_TITLE));
            String text = notesCursor.getString(notesCursor.getColumnIndex(DataBaseAccessNote.NoteTable.COLUMN_NAME_TEXT));
            LocalDateTime creationDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(notesCursor.getLong(notesCursor.getColumnIndex(DataBaseAccessNote.NoteTable.COLUMN_NAME_CREATIONDATE))),
                    TimeZone.getDefault().toZoneId());
            int id = notesCursor.getInt(notesCursor.getColumnIndex(DataBaseAccessNote.NoteTable.COLUMN_NAME_ID));
            Note note = new Note(id, title, text, creationDate, MyApplication.getCurrentLecture());
            _notes.add(note);
        }

        _listAdapter = new ArrayAdapter<Note>
                (this, android.R.layout.simple_list_item_1, _notes);
        notesListView.setAdapter(_listAdapter);
        notesListView.invalidateViews();
        _listAdapter.notifyDataSetChanged();
    }
}
