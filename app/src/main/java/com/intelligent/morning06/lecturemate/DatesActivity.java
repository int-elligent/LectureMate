package com.intelligent.morning06.lecturemate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.intelligent.morning06.lecturemate.DataAccess.MyDate;
import com.intelligent.morning06.lecturemate.Utils.DateTimeUtils;

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
    TextView descriptionEditText;
    private ArrayList<MyDate> _dates;
    private DateTimeFormatter _absoluteDTF;
    private DateTimeFormatter _relativeDTF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_view);

        absoluteDateTextView = (TextView)findViewById(R.id.absoluteDateTextView);
        relativeDateTextView = (TextView)findViewById(R.id.relativeDateTextView);
        descriptionEditText = (TextView) findViewById(R.id.descriptionEditText);

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
        menuInflater.inflate(R.menu.menu_dates_view, menu);

        MenuItem m0 =  menu.getItem(0);
        m0.setIcon(R.drawable.left_active);

        MenuItem m1 =  menu.getItem(1);
        m1.setIcon(R.drawable.right_active);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_left)
            switchDate(true);
        else
            switchDate(false);

        invalidateOptionsMenu();
        return true;
    }

    public void updateContent(int date_index){
        getSupportActionBar().setTitle(_dates.get(date_index).getTitle());
        absoluteDateTextView.setText(_dates.get(date_index).getDate().format(_absoluteDTF));
        relativeDateTextView.setText(DateTimeUtils.getRelativeDate(_dates.get(date_index).getDate()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        descriptionEditText.setText(_dates.get(date_index).getText());
    }

    private void switchDate(boolean left) {
        if(left) {
            if(_selectedIndex == 0)
                _selectedIndex = _dates.size() - 1;
            else
                _selectedIndex--;
        }
        else {
            if(_selectedIndex == _dates.size() - 1)
                _selectedIndex = 0;
            else
                _selectedIndex++;
        }

        updateContent(_selectedIndex);
    }
}
