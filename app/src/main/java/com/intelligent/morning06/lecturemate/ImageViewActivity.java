package com.intelligent.morning06.lecturemate;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ImageViewActivity extends AppCompatActivity {
    ViewPager viewPager;
    int images[] = {R.drawable.image1, R.drawable.image2, R.drawable.image3};
    ImageViewPagerAdapter imageViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        viewPager = (ViewPager)findViewById(R.id.viewPager);

        imageViewPagerAdapter = new ImageViewPagerAdapter(ImageViewActivity.this, images);
        viewPager.setAdapter(imageViewPagerAdapter);
    }
}

// reference https://www.thecrazyprogrammer.com/2016/12/android-image-slider-using-viewpager-example.html