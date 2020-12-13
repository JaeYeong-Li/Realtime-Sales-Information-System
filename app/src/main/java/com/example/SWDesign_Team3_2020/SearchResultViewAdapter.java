package com.example.SWDesign_Team3_2020;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import java.util.ArrayList;

public class SearchResultViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SearchResultViewItem> listViewItemList = null;

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
