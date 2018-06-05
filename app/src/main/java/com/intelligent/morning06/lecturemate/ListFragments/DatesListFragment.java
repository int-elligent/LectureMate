package com.intelligent.morning06.lecturemate.ListFragments;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.intelligent.morning06.lecturemate.Adapters.DatesAdapter;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessDate;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.MyDate;
import com.intelligent.morning06.lecturemate.DatesActivity;
import com.intelligent.morning06.lecturemate.DatesCreateActivity;
import com.intelligent.morning06.lecturemate.Interfaces.ICategoryListFragment;
import com.intelligent.morning06.lecturemate.MyApplication;
import com.intelligent.morning06.lecturemate.R;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimeZone;

public class DatesListFragment extends Fragment implements ICategoryListFragment {

    ListView _datesListView;
    ArrayList<MyDate> _allDates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dates_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        _datesListView = getActivity().findViewById(R.id.dates_list_listview);
        updateDates();
        _datesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openDate(i);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateDates();
    }

    public void openDate(int selectedIndex) {
        Intent intent = new Intent(getActivity(), DatesActivity.class);
        Bundle datesBundle = new Bundle();
        datesBundle.putSerializable("ALL_DATES", _allDates);
        intent.putExtra("SERIALIZED_DATA", datesBundle);
        intent.putExtra("SELECTED_INDEX", selectedIndex);
        startActivity(intent);
    }

    public void updateDates() {
        int lectureId = MyApplication.getCurrentLecture();
        if(_allDates != null)
            _allDates.clear();
        _allDates = new ArrayList<MyDate>();
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
            MyDate newdate = new MyDate(id, title, text, creationDate, MyApplication.getCurrentLecture(), date);
            _allDates.add(newdate);
        }

        _datesListView.setAdapter(new DatesAdapter(_allDates, getContext()));
    }

    @Override
    public void onFloatingActionButtonClicked() {
        Intent intent = new Intent(getActivity(), DatesCreateActivity.class);
        startActivityForResult(intent, 0);
    }
}
