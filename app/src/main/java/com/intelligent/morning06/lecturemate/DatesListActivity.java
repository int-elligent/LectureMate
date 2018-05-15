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

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessDate;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Dates;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimeZone;

public class DatesListActivity extends AppCompatActivity {

    private ListView datesListView;
    private String lectureNameToShow;
    private ArrayList<Dates> _dates = null;
    private ArrayAdapter<Dates> _listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_list);

        Intent intent = getIntent();
        lectureNameToShow = intent.getStringExtra("LectureName");
        setTitle(lectureNameToShow + " - Dates");

        datesListView = (ListView)findViewById(R.id.datesListView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_date_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateDateActivity();
            }
        });

        updateDates();

        datesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String lectureNameToShowCategories = lectures.get(i).getLectureName();
                openDate(i);
            }
        });

    }

    public void openCreateDateActivity()
    {
        //TODO
        //Intent intent = new Intent(this, DatesCreateActivity.class);
        //startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateDates();
    }

    public void openDate(int selectedIndex) {
        Intent intent = new Intent(DatesListActivity.this, DatesActivity.class);
        Bundle datesBundle = new Bundle();
        datesBundle.putSerializable("ALL_DATES", _dates);
        intent.putExtra("SERIALIZED_DATA", datesBundle);
        intent.putExtra("SELECTED_INDEX", selectedIndex);
        startActivity(intent);
    }

    public void updateDates() {
        int lectureId = MyApplication.getCurrentLecture();
        if(_dates != null)
            _dates.clear();
        _dates = new ArrayList<Dates>();
        Cursor datesCursor = DataModel.GetInstance().getDateDataBase().GetDateCursorForLecture(lectureId);

        while(datesCursor.moveToNext())
        {
            String title = datesCursor.getString(datesCursor.getColumnIndex(DataBaseAccessDate.DateTable.COLUMN_NAME_TITLE));
            String text = datesCursor.getString(datesCursor.getColumnIndex(DataBaseAccessDate.DateTable.COLUMN_NAME_TEXT));

            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(datesCursor.getLong(datesCursor.getColumnIndex(DataBaseAccessDate.DateTable.COLUMN_NAME_DATE))),
                    TimeZone.getDefault().toZoneId());
            LocalDateTime creationDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(datesCursor.getLong(datesCursor.getColumnIndex(DataBaseAccessDate.DateTable.COLUMN_NAME_CREATIONDATE))),
                    TimeZone.getDefault().toZoneId());
            int id = datesCursor.getInt(datesCursor.getColumnIndex(DataBaseAccessDate.DateTable.COLUMN_NAME_ID));
            Dates newdate = new Dates(id, title, text, creationDate, MyApplication.getCurrentLecture(), date);
            _dates.add(newdate);
        }

        _listAdapter = new ArrayAdapter<Dates>
                (this, android.R.layout.simple_list_item_1, _dates);
        datesListView.setAdapter(_listAdapter);
        datesListView.invalidateViews();
        _listAdapter.notifyDataSetChanged();
    }
}
