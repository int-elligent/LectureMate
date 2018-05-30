package com.intelligent.morning06.lecturemate.Adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.intelligent.morning06.lecturemate.DataAccess.Image;
import com.intelligent.morning06.lecturemate.R;

import java.util.ArrayList;

public class ImagesAdapter extends DateHeaderListAdapter<Image> {

    public ImagesAdapter(ArrayList<Image> images, Context context) {
        super(images, context);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(_context).
                    inflate(R.layout.images_list_item, parent, false);
        }

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.images_list_item_thumbnail);
        Uri imagePath = Uri.parse(_views.get(position)._item.getFilePath());

        Glide.with(_context)
                .load(imagePath)
                .apply(RequestOptions.centerCropTransform())
                .into(thumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.images_list_item_title);
        title.setText(_views.get(position)._item.getTitle());

        return convertView;
    }
}
