package com.customerkoi.todoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.customerkoi.todoapp.models.Category;
import com.ocufoxtech.firebasetodoruia.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    ArrayList<Category> categoryArrayList;

    public CustomAdapter(Context applicationContext, ArrayList<Category> categoryArrayList) {
        this.context = applicationContext;
        this.categoryArrayList = categoryArrayList;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return categoryArrayList.size();

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView names = (TextView) view.findViewById(R.id.textView);
        names.setText(categoryArrayList.get(i).getCategory_name());
        return view;
    }
}