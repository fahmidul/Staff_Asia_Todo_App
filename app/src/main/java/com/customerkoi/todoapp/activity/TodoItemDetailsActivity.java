package com.customerkoi.todoapp.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ocufoxtech.firebasetodoruia.R;

public class TodoItemDetailsActivity extends AppCompatActivity {

    Toolbar toolBar;

    TextView tvStatus, tvTitle, tvDescription, tvCategory;
    String title, description, category, status;
    public static String TAG = "firebase_todo_app";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "in TodoItemDetailsActivity");
        setContentView(R.layout.activity_todo_items_details);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        category = getIntent().getStringExtra("category");
        status = getIntent().getStringExtra("status");

        init();

    }

    public void init() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvCategory = (TextView) findViewById(R.id.tvCategory);


        tvTitle.setText(title);
        tvDescription.setText(description);
        tvCategory.setText(category);

        if (status.equals("1")) {
            tvStatus.setText("Task Status: " + "Completed");
            tvStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else {
            tvStatus.setText("Task Status: " + "Pending");
            tvStatus.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}