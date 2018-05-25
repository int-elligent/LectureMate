package com.intelligent.morning06.lecturemate;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;

import java.time.Instant;

public class ImagesCreateActivity extends AppCompatActivity {

    Uri _imageUri;

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
                    setResult(ImagesListActivity.EMPTY_REQUEST_CODE);
                    finish();
                }
                return true;
            }
        });

        _imageUri = Uri.parse(getIntent().getStringExtra("IMAGE_URI"));
        ImageView imageView = (ImageView) findViewById(R.id.editImage);

        Toast.makeText(this, "uri: " + _imageUri.toString(), Toast.LENGTH_LONG).show();

        Glide.with(getApplicationContext())
                .load(_imageUri)
                .apply(RequestOptions.centerCropTransform())
                .into(imageView);
    }

    private void SaveImage() {

        String text = ((EditText)findViewById(R.id.editTextImage)).getText().toString();

        if (text.isEmpty()) {
            ShowToast("Image text cannot be empty");
            return;
        }

        try {
            DataModel.GetInstance().getImageDataBase().AddImage(text, Instant.now().toEpochMilli(), _imageUri.toString(), MyApplication.getCurrentLecture());
        } catch(Exception exception) {
            ShowToast("Could not add image to database: " + exception.getMessage());
            return;
        }

        setResult(ImagesListActivity.EMPTY_REQUEST_CODE);
        finish();
    }

    private void ShowToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}

