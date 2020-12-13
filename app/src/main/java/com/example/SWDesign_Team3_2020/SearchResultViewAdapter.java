package com.example.SWDesign_Team3_2020;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.widget.TextView;

public class SearchResultViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SearchResultViewItem> listViewItemList = null;

    public SearchResultViewAdapter() {
    }

    public SearchResultViewAdapter(Context mcontext, ArrayList<SearchResultViewItem> list) {
        super();
        this.mContext = mcontext;
        listViewItemList = list;
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public SearchResultViewItem getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "fragment4_view" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.searchresult_item, parent, false);
        }

        /* 'fragment4_view'에 정의된 위젯에 대한 참조 획득 */
        TextView storenameView = (TextView) convertView.findViewById(R.id.sritem_storename);
        TextView addressView = (TextView) convertView.findViewById(R.id.sritem_address);
        TextView opentime = (TextView) convertView.findViewById(R.id.sritem_opentime);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SearchResultViewItem listViewItem = listViewItemList.get(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        storenameView.setText(listViewItem.getStoreName());
        addressView.setText(listViewItem.getAddress());
        opentime.setText(listViewItem.getOpenTime());


        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)*/
        /*
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "선택되었다", Toast.LENGTH_SHORT).show();
            }
        });*/
        return convertView;
    }


}
