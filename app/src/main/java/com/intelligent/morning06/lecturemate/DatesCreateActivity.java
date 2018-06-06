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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Locale;

public class DatesCreateActivity extends AppCompatActivity {

    private int _lectureId;
    EditText dateselect;
    DatePickerDialog datePickerDialog;
    EditText timeselect;
    TimePickerDialog timePickerDialog;
    final Calendar calendar = Calendar.getInstance();


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
                final int mYear = calendar.get(Calendar.YEAR);
                final int mMonth = calendar.get(Calendar.MONTH);
                final int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(DatesCreateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                String dayText = "";
                                String monthText = "";
                                if(day < 10)
                                    dayText = "0" + day;
                                else
                                    dayText = "" + day;

                                if(month < 9)
                                    monthText = "0" + (month+1);
                                else
                                    monthText = "" + (month+1);

                                dateselect.setText(dayText + "/" + monthText + "/" + year);
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
                final int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                final int mMinute = calendar.get(Calendar.MINUTE);


                timePickerDialog = new TimePickerDialog(DatesCreateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                String hourtext = "";
                                String minutetext = "";
                                if(hour < 10)
                                    hourtext = "0" + hour;
                                else
                                    hourtext = "" + hour;

                                if(minute < 10)
                                    minutetext = "0" + minute;
                                else
                                    minutetext = "" + minute;

                                timeselect.setText(hourtext + ":" + minutetext);
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

        LocalDate dateObject = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDateTime dateTimeObject = LocalDateTime.of(dateObject, LocalTime.parse(time, DateTimeFormatter.ofPattern("kk:mm")));


        try {
            DataModel.GetInstance().getDateDataBase().AddDate(title, text, Instant.now().toEpochMilli(), dateTimeObject.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),  MyApplication.getCurrentLecture());
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
