package com.intelligent.morning06.lecturemate.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intelligent.morning06.lecturemate.DataAccess.MyDate;
import com.intelligent.morning06.lecturemate.DataAccess.Note;
import com.intelligent.morning06.lecturemate.R;
import com.intelligent.morning06.lecturemate.Utils.DateTimeUtils;

import java.util.ArrayList;

public class DatesAdapter extends DateHeaderListAdapter<MyDate> {

    public DatesAdapter(ArrayList<MyDate> dates, Context context) {
        super(dates, context);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(_context).
                    inflate(R.layout.dates_list_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.dates_list_itemTitle);
        TextView date = (TextView) convertView.findViewById(R.id.dates_list_itemDate);
        TextView time = (TextView) convertView.findViewById(R.id.dates_list_itemTime);

        title.setText(_views.get(position)._item.getTitle());
        time.setText(DateTimeUtils.FormatDateTimeAsNormalTime(_views.get(position)._item.getDate()));
        date.setText(DateTimeUtils.FormatDateTimeAsNormalDate(_views.get(position)._item.getDate()));

        return convertView;
    }
}
