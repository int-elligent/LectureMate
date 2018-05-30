package com.intelligent.morning06.lecturemate;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.intelligent.morning06.lecturemate.Adapters.ImageViewPagerAdapter;
import com.intelligent.morning06.lecturemate.DataAccess.Image;
import com.intelligent.morning06.lecturemate.DataAccess.Note;

import java.util.ArrayList;

public class ImageViewActivity extends AppCompatActivity {
    ViewPager viewPager;
    ArrayList<Image> images;// = {R.drawable.image1, R.drawable.image2, R.drawable.image3};
    int image_index;
    ImageViewPagerAdapter imageViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);


        viewPager = (ViewPager)findViewById(R.id.viewPager);

        Bundle allImages = getIntent().getBundleExtra("SERIALIZED_DATA");
        images = (ArrayList<Image>) allImages.getSerializable("ALL_IMAGES");
        image_index = getIntent().getIntExtra("SELECTED_INDEX", 0);

        imageViewPagerAdapter = new ImageViewPagerAdapter(ImageViewActivity.this, images, image_index);
        viewPager.setAdapter(imageViewPagerAdapter);
        viewPager.setCurrentItem(image_index);

    }
}
// reference https://www.thecrazyprogrammer.com/2016/12/android-image-slider-using-viewpager-example.html