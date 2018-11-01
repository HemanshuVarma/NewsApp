package com.varma.hemanshu.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> newsArrayList) {
        super(context, 0, newsArrayList);
    }

    static class ViewHolder {
        private TextView title;
        private TextView author;
        private TextView category;
        private TextView date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_items, parent, false);

            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.title_tv);
            holder.author = convertView.findViewById(R.id.author_tv);
            holder.category = convertView.findViewById(R.id.category_tv);
            holder.date = convertView.findViewById(R.id.date_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        News currentData = getItem(position);

        holder.title.setText(currentData.getTitle());
        holder.author.setText(currentData.getAuthor());
        holder.category.setText(currentData.getCategory());

        String dateTimeStamp = currentData.getTimestamp();
        String[] dateString = dateTimeStamp.split("T");
        holder.date.setText(dateString[0]);
        return convertView;
    }

}
