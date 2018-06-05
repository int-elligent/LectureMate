package com.intelligent.morning06.lecturemate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessDate;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Locale;

public class DatesCreateActivity extends AppCompatActivity {

    private int _lectureId;
    EditText dateselect;
    DatePickerDialog datePickerDialog;
    EditText timeselect;
    TimePickerDialog timePickerDialog;
    final Calendar c = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_create);

        dateselect =  findViewById(R.id.Date);
        dateselect.setFocusable(false);
        dateselect.setKeyListener(null);

        dateselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH);
                final int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(DatesCreateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                dateselect.setText(day + "/" + month + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        timeselect =  findViewById(R.id.Time);
        timeselect.setFocusable(false);
        timeselect.setKeyListener(null);

        timeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int mHour = c.get(Calendar.HOUR_OF_DAY);
                final int mMinute = c.get(Calendar.MINUTE);


                timePickerDialog = new TimePickerDialog(DatesCreateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                timeselect.setText(hour + ":" + minute);
                            }
                        }, mHour, mMinute,true);
                timePickerDialog.show();
            }
        });




        Toolbar toolbar = (Toolbar) findViewById(R.id.create_dates_toolbar);
        toolbar.inflateMenu(R.menu.menu_dates_create_activity);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.dates_create_activity_action_save) {
                    SaveDate();
                } else {
                    finish();
                }
                return true;
            }
        });




    }


    private void SaveDate() {
        String title = ((EditText)findViewById(R.id.editTextTitle)).getText().toString();
        String text = ((EditText)findViewById(R.id.editTextDate)).getText().toString();
        String time = ((EditText)findViewById(R.id.Time)).getText().toString();
        String date = ((EditText)findViewById(R.id.Date)).getText().toString();


        if(title.isEmpty()) {
            ShowToast("Title cannot be empty");
            return;
        }

        if (text.isEmpty()) {
            ShowToast("Date text cannot be empty");
            return;
        }
        if(date.isEmpty()) {
            ShowToast("Date must be selected");
            return;
        }
        if(time.isEmpty()) {
            ShowToast("Time must be selected");
            return;
        }


        try {
            DataModel.GetInstance().getDateDataBase().AddDate(title, text, c.getTimeInMillis(), Instant.now().toEpochMilli(), MyApplication.getCurrentLecture());
        } catch(SQLException exception) {
            ShowToast("Could not add date to database: " + exception.getMessage());
            return;
        }

        finish();
    }

    private void ShowToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}
