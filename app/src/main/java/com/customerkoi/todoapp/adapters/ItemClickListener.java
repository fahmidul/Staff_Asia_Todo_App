package com.customerkoi.todoapp.adapters;

import android.view.View;

/**
 * Created by Gautam on 11-11-2017.
 */

public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
