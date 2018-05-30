package com.intelligent.morning06.lecturemate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.intelligent.morning06.lecturemate.DataAccess.MyDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class DatesActivity extends AppCompatActivity {

    int total_dates;
    private int _selectedIndex;
    TextView absoluteDateTextView;
    TextView relativeDateTextView;
    EditText descriptionEditText;
    private ArrayList<MyDate> _dates;
    private DateTimeFormatter _absoluteDTF;
    private DateTimeFormatter _relativeDTF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_view);

        absoluteDateTextView = (TextView)findViewById(R.id.absoluteDateTextView);
        relativeDateTextView = (TextView)findViewById(R.id.relativeDateTextView);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);

        _absoluteDTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Bundle allDates = getIntent().getBundleExtra("SERIALIZED_DATA");
        _dates = (ArrayList<MyDate>) allDates.getSerializable("ALL_DATES");
        _selectedIndex = getIntent().getIntExtra("SELECTED_INDEX", 0);

        total_dates = _dates.size();
        updateContent(_selectedIndex);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_dates, menu);

        MenuItem m0 =  menu.getItem(0);
        if(_selectedIndex > 0){
            m0.setIcon(R.drawable.left_active);
        }else{
            m0.setIcon(R.drawable.left_passive);
        }

        MenuItem m1 =  menu.getItem(1);
        if (_selectedIndex < total_dates - 1){
            m1.setIcon(R.drawable.right_active);
        }else{
            m1.setIcon(R.drawable.right_passive);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if(res_id == R.id.action_left && _selectedIndex < total_dates - 1){
            _dates.get(_selectedIndex).setText(descriptionEditText.getText().toString());
            _selectedIndex += 1;
            updateContent(_selectedIndex);
            invalidateOptionsMenu();
        }else if(res_id == R.id.action_right && _selectedIndex > 0){
            _dates.get(_selectedIndex).setText(descriptionEditText.getText().toString());
            _selectedIndex -= 1;
            updateContent(_selectedIndex);
            invalidateOptionsMenu();
        }
        return true;
    }

    public void updateContent(int date_index){
        getSupportActionBar().setTitle(_dates.get(date_index).getTitle());
        absoluteDateTextView.setText(_dates.get(date_index).getDate().format(_absoluteDTF));
        relativeDateTextView.setText(getRelativeDate(_dates.get(date_index).getDate()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        descriptionEditText.setText(_dates.get(date_index).getText());
    }



    public String getRelativeDate(String reference_date) {
        SimpleDateFormat relativeDF = new SimpleDateFormat("dd/MM/yyyy");
        long mpd = 86400000;

        try {
            Date reference = relativeDF.parse(reference_date);
            Date today_raw = new Date(System.currentTimeMillis());
            Date today_processed = relativeDF.parse(relativeDF.format(today_raw));
            long diff = (reference.getTime() - today_processed.getTime())/mpd;

            if (diff == 0){
                return  "Today";
            }else if (diff == -1){
                return "Yesterday";
            }else if (diff < -1){
                return (diff * (-1)) + " days ago";
            }else if (diff == 1){
                return "Tomorrow";
            }else if (diff > 1){
                return diff + " days left";
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return  "ERROR";
    }


    @Override
    protected void onStop() {
        super.onStop();
        //TODO update the description in database
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO update the description in database
    }
}
