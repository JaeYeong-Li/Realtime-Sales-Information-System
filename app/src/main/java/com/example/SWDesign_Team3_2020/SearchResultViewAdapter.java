package com.example.SWDesign_Team3_2020;

import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

public class SearchResultViewAdapter extends RecyclerView.Adapter<SearchResultViewAdapter.CustomViewHolder> {
    private ArrayList<SearchResultViewItem> mList = null;
    private Activity context = null;


    public SearchResultViewAdapter(Activity context, ArrayList<SearchResultViewItem> list) {
            this.context = context;
            this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView storename;
        protected TextView address;
        protected TextView opentime;


        public CustomViewHolder(View view) {
            super(view);
            this.storename = (TextView) view.findViewById(R.id.sritem_storename);
            this.address = (TextView) view.findViewById(R.id.sritem_address);
            this.opentime = (TextView) view.findViewById(R.id.sritem_opentime);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchresult_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.storename.setText(mList.get(position).getStoreName());
        viewholder.address.setText(mList.get(position).getAddress());
        viewholder.opentime.setText(mList.get(position).getOpenTime());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }


}
