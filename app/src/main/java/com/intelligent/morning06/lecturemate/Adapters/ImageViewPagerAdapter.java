package com.intelligent.morning06.lecturemate.Adapters;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import com.intelligent.morning06.lecturemate.DataAccess.Image;
import com.intelligent.morning06.lecturemate.R;

import java.io.File;
import java.util.ArrayList;

public class ImageViewPagerAdapter extends PagerAdapter{
    File imgFile;
    Bitmap imgBitmap;
    Context context;
    ArrayList<Image> images;
    int image_index;
    LayoutInflater layoutInflater;

    public ImageViewPagerAdapter(Context context, ArrayList<Image> images, int image_index) {
        this.context = context;
        this.images = images;
        this.image_index = image_index;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.activity_screen_slide, container, false);

        imgFile = new File(images.get(position).getFilePath());
        imgBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageBitmap(imgBitmap);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

// reference https://www.thecrazyprogrammer.com/2016/12/android-image-slider-using-viewpager-example.html