package com.example.amit.moviebuzz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<BookmarkData> arrayList;
    MyAdapter(Context context,ArrayList<BookmarkData> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }


    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(R.layout.bookmark_single,null);
        TextView mTitle=convertView.findViewById(R.id.bookmark_title_tv);

        BookmarkData bookmarkData=arrayList.get(position);
        mTitle.setText(bookmarkData.getmTitle());

        return convertView;

    }
}
