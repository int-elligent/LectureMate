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

import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessImage;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessNote;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class ImagesCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_create);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_images_toolbar);
        toolbar.inflateMenu(R.menu.menu_images_create_activity);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.images_create_activity_action_save) {
                   SaveImage();
                } else if (menuItem.getItemId() == R.id.images_create_activity_action_cancel) {
                    finish();
                }
                return true;
            }
        });
    }

    private void SaveImage() {
        String text = ((EditText)findViewById(R.id.editTextImage)).getText().toString();

        if (text.isEmpty()) {
            ShowToast("Image text cannot be empty");
            return;
        }

        try {
            DataModel.GetInstance().getImageDataBase().AddImage(text, Instant.now().toEpochMilli(), "@android:drawable/btn_dialog", MyApplication.getCurrentLecture());
        } catch(Exception exception) {
            ShowToast("Could not add image to database: " + exception.getMessage());
            return;
        }

        finish();
    }

    private void ShowToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}

