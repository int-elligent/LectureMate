package com.intelligent.morning06.lecturemate;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
    protected ListView imageListView;
    ArrayList<Image> allImages = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.images_list_toolbar);
        setSupportActionBar(toolbar);


        imageListView = (ListView)findViewById(R.id.images_list_listview);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.images_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        updateImageList();
        imageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String lectureNameToShowCategories = lectures.get(i).getLectureName();
                openImages(i);
            }
        });
    }

    public void openImages(int selectedIndex) {
        int size = selectedIndex;
        for( int j = 0; j < size; j++)
            if(((ListView)findViewById(R.id.images_list_listview)).getAdapter().getItemViewType(j) == 1) selectedIndex--;
        Intent intent = new Intent(ImagesListActivity.this, ImageViewActivity.class);
        Bundle ImageBundle = new Bundle();
        ImageBundle.putSerializable("ALL_IMAGES", allImages);
        intent.putExtra("SERIALIZED_DATA", ImageBundle);
        intent.putExtra("SELECTED_INDEX", selectedIndex);
        //Log.e("ERROR",selectedIndex+"");
        startActivity(intent);
    }

    private void updateImageList() {
        Cursor imageCursor = DataModel.GetInstance().getImageDataBase().
                GetImageCursorForLecture(MyApplication.getCurrentLecture());

        allImages = new ArrayList<Image>();

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
