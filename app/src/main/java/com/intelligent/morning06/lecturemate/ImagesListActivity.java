package com.intelligent.morning06.lecturemate;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.intelligent.morning06.lecturemate.Adapters.ImagesAdapter;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessImage;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Image;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimeZone;

public class ImagesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.images_list_toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.images_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        updateImageList();
    }

    private void updateImageList() {
        Cursor imageCursor = DataModel.GetInstance().getImageDataBase().
                GetImageCursorForLecture(MyApplication.getCurrentLecture());

        ArrayList<Image> allImages = new ArrayList<Image>();

        while(imageCursor.moveToNext()) {
            String title = imageCursor.
                    getString(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_TITLE));
            String filePath = imageCursor.
                    getString(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_FILEPATH));
            int id = imageCursor.
                    getInt(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_ID));
            LocalDateTime creationDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(
                    imageCursor.getLong(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_CREATIONDATE))),
                    TimeZone.getDefault().toZoneId());

            Image image = new Image(id, title, filePath, creationDate, MyApplication.getCurrentLecture());

            allImages.add(image);
        }

        ImagesAdapter imagesListAdapter = new ImagesAdapter(allImages, getApplicationContext());

        ((ListView)findViewById(R.id.images_list_listview)).setAdapter(imagesListAdapter);

    }

}
