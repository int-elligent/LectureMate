package com.intelligent.morning06.lecturemate.Adapters;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.intelligent.morning06.lecturemate.DataAccess.Image;
import com.intelligent.morning06.lecturemate.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class ImagesAdapter extends BaseAdapter {

    public class ViewType {
        public final static int ITEM = 0;
        public final static int SEPARATOR = 1;
    }

    class ImageHolder {
        int _viewType;
        public Image _image;
        public String _separatorText;
    }

    private ArrayList<ImageHolder> _views;
    private Context _context;

    public ImagesAdapter(ArrayList<Image> images, Context context) {
        _context = context;
        _views = new ArrayList<ImageHolder>();
        if(images.size() <= 0)
            return;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        images.sort(new Comparator<Image>() {
            @Override
            public int compare(Image o1, Image o2) {
                return(o1.getCreationDate().compareTo(o2.getCreationDate()));
            }
        });

        LocalDateTime lastDate = images.get(0).getCreationDate();
        ImageHolder firstSeparator = new ImageHolder();
        firstSeparator._viewType = ViewType.SEPARATOR;
        firstSeparator._separatorText = lastDate.format(formatter);
        _views.add(firstSeparator);

        for(int index=0; index < images.size(); index++) {
            if(!(images.get(index).getCreationDate().format(formatter).equals(lastDate.format(formatter)))) {
                lastDate = images.get(index).getCreationDate();
                ImageHolder separatorHolder = new ImageHolder();
                separatorHolder._viewType = ViewType.SEPARATOR;
                separatorHolder._separatorText = lastDate.format(formatter);
                _views.add(separatorHolder);
            }
            ImageHolder imageHolder = new ImageHolder();
            imageHolder._image = images.get(index);
            imageHolder._viewType = ViewType.ITEM;
            _views.add(imageHolder);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return _views.get(position)._viewType;
    }

    @Override
    public int getCount() {
        return _views.size();
    }

    @Override
    public Object getItem(int position) {
        return _views.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() { return false; }

    @Override
    public boolean isEnabled(int position) {
        return (_views.get(position)._viewType == ViewType.ITEM);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            switch(getItemViewType(position)) {
                case ViewType.ITEM:
                    if (convertView == null) {
                        convertView = LayoutInflater.from(_context).
                                inflate(R.layout.activity_images_list_listview_item, parent, false);
                    }

                    ImageView thumbnail = (ImageView) convertView.findViewById(R.id.images_list_item_thumbnail);
                    Uri imagePath = Uri.parse(_views.get(position)._image.getFilePath());

                    Glide.with(_context)
                            .load(imagePath)
                            .apply(RequestOptions.centerCropTransform())
                            .into(thumbnail);

                    TextView title = (TextView) convertView.findViewById(R.id.images_list_item_title);
                    title.setText(_views.get(position)._image.getTitle());
                    break;
                case ViewType.SEPARATOR:
                    if(convertView == null) {
                        convertView = LayoutInflater.from(_context).
                                inflate(R.layout.activity_images_list_listview_separator, parent, false);
                    }
                    TextView groupHeading = (TextView) convertView.findViewById(R.id.images_list_item_separator_text);
                    groupHeading.setText(_views.get(position)._separatorText);
                    break;
            }

        return convertView;
    }
}
