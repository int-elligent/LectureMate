package com.intelligent.morning06.lecturemate.Adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.intelligent.morning06.lecturemate.DataAccess.Image;
import com.intelligent.morning06.lecturemate.Interfaces.IDateSortable;
import com.intelligent.morning06.lecturemate.R;
import com.intelligent.morning06.lecturemate.Utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public abstract class DateHeaderListAdapter<T extends IDateSortable> extends BaseAdapter {

    protected static class ViewType {
        public final static int ITEM = 0;
        public final static int SEPARATOR = 1;
    }

    protected class DateSortableHolder {
        int _viewType;
        public T _item;
        public String _separatorText;
    }

    protected ArrayList<DateSortableHolder> _views;
    protected Context _context;

    public DateHeaderListAdapter(ArrayList<T> items, Context context) {
        _context = context;
        _views = new ArrayList<DateSortableHolder>();
        if(items.size() <= 0)
            return;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("L yyyy");

        items.sort(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return(o2.getCreationDate().compareTo(o1.getCreationDate()));
            }
        });

        LocalDateTime lastDate = items.get(0).getCreationDate();
        DateSortableHolder firstSeparator = new DateSortableHolder();
        firstSeparator._viewType = ViewType.SEPARATOR;
        firstSeparator._separatorText = DateTimeUtils.FormatDateTimeToMonthAndYear(lastDate);
        _views.add(firstSeparator);

        for(int index=0; index < items.size(); index++) {
            LocalDateTime currentDateTime = items.get(index).getCreationDate();
            if(!(DateTimeUtils.FormatDateTimeToMonthAndYear(currentDateTime).equals(DateTimeUtils.FormatDateTimeToMonthAndYear(lastDate)))) {
                lastDate = items.get(index).getCreationDate();
                DateSortableHolder separatorHolder = new DateSortableHolder();
                separatorHolder._viewType = ViewType.SEPARATOR;
                separatorHolder._separatorText = DateTimeUtils.FormatDateTimeToMonthAndYear(lastDate);
                _views.add(separatorHolder);
            }
            DateSortableHolder viewHolder = new DateSortableHolder();
            viewHolder._item = items.get(index);
            viewHolder._viewType = ViewType.ITEM;
            _views.add(viewHolder);
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
        return _views.get(position)._item;
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
        if(getItemViewType(position) == ViewType.ITEM) {
            return getItemView(position, convertView, parent);
        }
        else {
            if(convertView == null) {
                convertView = LayoutInflater.from(_context).
                        inflate(R.layout.category_list_date_separator, parent, false);
            }
            TextView groupHeading = (TextView) convertView.findViewById(R.id.images_list_item_separator_text);
            groupHeading.setText(_views.get(position)._separatorText);
            return convertView;
        }
    }

    public abstract View getItemView(int position, View convertView, ViewGroup parent);
}
