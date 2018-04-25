package com.intelligent.morning06.lecturemate.DataAccess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.intelligent.morning06.lecturemate.R;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity {

    private ListView notesListView;
    public String[] names;
    public String[] previews;
    public String[] creation_dates;
    private String lectureNameToShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        Intent intent = getIntent();
        lectureNameToShow = intent.getStringExtra("LectureName");
        setTitle(lectureNameToShow + " - Notes");

        notesListView = (ListView)findViewById(R.id.notesListView);

        //TODO: replace these with appropriate values from database
        names = new String[] {  "Note 1",
                                "Note 2",
                                "Note 3"};
        previews = new String[]{    "The first note is about...",
                                    "The second note is about...",
                                    "The third note is about..."};
        creation_dates = new String[]{  "13/04/2018",
                                        "14/04/2018",
                                        "15/04/2018"};

        NotesAdapter notesAdapter = new NotesAdapter(this, names, previews, creation_dates);
        notesListView.setAdapter(notesAdapter);

    }

    public int getNumberOfNames(){
        return names.length;
    }
}
