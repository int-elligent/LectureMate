package com.intelligent.morning06.lecturemate.ListFragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.intelligent.morning06.lecturemate.Adapters.NotesAdapter;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessNote;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Note;
import com.intelligent.morning06.lecturemate.Interfaces.ICategoryListFragment;
import com.intelligent.morning06.lecturemate.MyApplication;
import com.intelligent.morning06.lecturemate.NotesActivity;
import com.intelligent.morning06.lecturemate.NotesCreateActivity;
import com.intelligent.morning06.lecturemate.R;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimeZone;

public class NotesListFragment extends Fragment implements ICategoryListFragment{

    ListView _notesListView;
    ArrayList<Note> _allNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.notes_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        _notesListView = getActivity().findViewById(R.id.notes_list_listview);
        updateNotes();
        _notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openNote(i);
            }
        });
    }

    @Override
    public void onFloatingActionButtonClicked()
    {
        Intent intent = new Intent(getActivity(), NotesCreateActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateNotes();
    }

    public void openNote(int selectedIndex) {

        Intent intent = new Intent(getActivity(), NotesActivity.class);
        Bundle notesBundle = new Bundle();
        notesBundle.putSerializable(getResources().getString(R.string.intent_extra_allNotes), _allNotes);
        intent.putExtra(getResources().getString(R.string.intent_extra_serializedData), notesBundle);
        intent.putExtra(getResources().getString(R.string.intent_extra_selectedIndex), _allNotes.indexOf((_notesListView.getAdapter().getItem(selectedIndex))));
        startActivity(intent);
    }

    public void updateNotes() {
        int lectureId = MyApplication.getCurrentLecture();
        _allNotes = new ArrayList<Note>();
        Cursor notesCursor = DataModel.GetInstance().getNoteDataBase().GetNoteCursorForLecture(lectureId);

        while(notesCursor.moveToNext())
        {
            String title = notesCursor.getString(notesCursor.getColumnIndex(DataBaseAccessNote.NoteTable.COLUMN_NAME_TITLE));
            String text = notesCursor.getString(notesCursor.getColumnIndex(DataBaseAccessNote.NoteTable.COLUMN_NAME_TEXT));
            LocalDateTime creationDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(notesCursor.getLong(notesCursor.getColumnIndex(DataBaseAccessNote.NoteTable.COLUMN_NAME_CREATIONDATE))),
                    TimeZone.getDefault().toZoneId());
            int id = notesCursor.getInt(notesCursor.getColumnIndex(DataBaseAccessNote.NoteTable.COLUMN_NAME_ID));
            Note note = new Note(id, title, text, creationDate, MyApplication.getCurrentLecture());
            _allNotes.add(note);
        }

        _notesListView.setAdapter(new NotesAdapter(_allNotes, getContext()));
    }
}
