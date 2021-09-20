package com.example.burningbuilding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class scoreAdapter extends BaseAdapter {

    private List<scoreListItem> scoreList;
    private Context context;

    public scoreAdapter(List<scoreListItem> i_List, Context i_contect)
    {
        scoreList = i_List;
        context = i_contect;
    }

    @Override
    public int getCount() {
        return scoreList.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_layout, parent,false);
        }

        scoreListItem list_Item = scoreList.get(position);
        TextView p_Name = convertView.findViewById(R.id.player_name);
        TextView p_Score = convertView.findViewById(R.id.player_score);
        TextView p_Date = convertView.findViewById(R.id.player_date);

        p_Name.setText(list_Item.getName());
        p_Score.setText(list_Item.getScore() + "");
        p_Date.setText(list_Item.getDate());

        return convertView;
    }
}
