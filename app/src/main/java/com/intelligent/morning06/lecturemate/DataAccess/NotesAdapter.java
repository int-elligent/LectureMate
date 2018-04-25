package com.intelligent.morning06.lecturemate.DataAccess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.intelligent.morning06.lecturemate.R;

public class NotesAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    String[] names;
    String[] previews;
    String[] creation_dates;

    public NotesAdapter(Context c, String[] n, String[] p, String[] d){
        names = n;
        previews = p;
        creation_dates = d;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int i) {
        return names[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.notes_listview_detail, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView previewTextView = (TextView) v.findViewById(R.id.previewTextView);
        TextView creationDateTextView = (TextView) v.findViewById(R.id.creationDateTextView);

        String name = names[i];
        String preview = previews[i];
        String creation_date = creation_dates[i];

        nameTextView.setText(name);
        previewTextView.setText(preview);
        creationDateTextView.setText(creation_date);

        return v;
    }
}
