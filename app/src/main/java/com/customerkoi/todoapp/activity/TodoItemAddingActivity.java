package com.customerkoi.todoapp.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.customerkoi.todoapp.adapters.CustomAdapter;
import com.customerkoi.todoapp.dialogs.CustomDialogClass;
import com.customerkoi.todoapp.helpers.ValidateHelper;
import com.customerkoi.todoapp.models.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ocufoxtech.firebasetodoruia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TodoItemAddingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Toolbar toolBar;
    Spinner spinnerCategory;
    ArrayList<Category> categoryLists;
    String selectedCategoryId, selectedCategoryName;
    TextView tvSaveTask;
    EditText edtTaskTitle, edtDescription;
    FirebaseFirestore firebaseFirestore;
    String editableTaskId, editableTaskTitle, editableTaskDescription, editableCategoryId, editableCategoryName, editableStatus;
    String status = "0";

    TextView tvCategoryHeading;
    RelativeLayout relCategoryAddingView;

    public static String TAG = "firebase_todo_app";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_todo_item);

        editableTaskId = getIntent().getStringExtra("id");
        editableTaskTitle = getIntent().getStringExtra("title");
        editableTaskDescription = getIntent().getStringExtra("description");

        editableCategoryId = getIntent().getStringExtra("category_id");
        editableCategoryName = getIntent().getStringExtra("category_name");
        editableStatus = getIntent().getStringExtra("status");

        init();

    }

    public void init() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        categoryLists = new ArrayList<>();
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerCategory.setOnItemSelectedListener(this);

        tvCategoryHeading = (TextView) findViewById(R.id.tvCategoryHeading);
        relCategoryAddingView = (RelativeLayout) findViewById(R.id.relCategoryAddingView);

        edtTaskTitle = (EditText) findViewById(R.id.edtTaskTitle);
        edtDescription = (EditText) findViewById(R.id.edtDescription);
        tvSaveTask = (TextView) findViewById(R.id.tvSaveTask);


        firebaseFirestore = FirebaseFirestore.getInstance();

        if (editableTaskTitle != null) {
            edtTaskTitle.setText(editableTaskTitle);
        }
        if (editableTaskDescription != null) {
            edtDescription.setText(editableTaskDescription);
        }

        if (editableTaskId != null) {
            tvSaveTask.setText(getString(R.string.upadte_task));
            if (editableCategoryName != null && editableCategoryId != null && editableStatus != null) {
                tvCategoryHeading.setVisibility(View.GONE);
                relCategoryAddingView.setVisibility(View.GONE);
                selectedCategoryName = editableCategoryName;
                selectedCategoryId = editableCategoryId;
                status = editableStatus;
            } else {
                Toast.makeText(TodoItemAddingActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        }



        findViewById(R.id.tvAddNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogClass cdd = new CustomDialogClass(TodoItemAddingActivity.this);
                cdd.setCanceledOnTouchOutside(false);
                cdd.setCancelable(true);
                cdd.show();
            }
        });



        tvSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtTaskTitle.getText().toString().equals("") && !edtDescription.getText().toString().equals("")) {

                    if (!ValidateHelper.validateStringData(selectedCategoryId).equals("") || !ValidateHelper.validateStringData(selectedCategoryName).equals("")) {
                        FirebaseUser testUser = FirebaseAuth.getInstance().getCurrentUser(); //getting the current logged in users id
                        String userUid = testUser.getUid();
                        Map<String, Object> toDo = new HashMap<>();
                        toDo.put("id", userUid);
                        toDo.put("title", edtTaskTitle.getText().toString());
                        toDo.put("description", edtDescription.getText().toString());
                        toDo.put("category_id", selectedCategoryId);
                        toDo.put("category_name", selectedCategoryName);
                        toDo.put("status", status);
                        if (editableTaskId == null) {
                            firebaseFirestore.collection("todos").document().set(toDo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(TodoItemAddingActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        } else {

                            firebaseFirestore.collection("todos").document(editableTaskId).set(toDo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(TodoItemAddingActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.category_warning), Toast.LENGTH_LONG).show();

                    }


                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.fill_all_data_warning), Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSpinnerData();
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

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        selectedCategoryId = categoryLists.get(position).getId();
        selectedCategoryName = categoryLists.get(position).getCategory_name();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void getSpinnerData() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = user.getUid();//getting unique user id
        db.collection("todo_category").whereEqualTo("id", userUid).get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        categoryLists.clear();
                        for (DocumentSnapshot doc : task.getResult()) {

                            Category category = doc.toObject(Category.class);
                            category.setId(doc.getId());
                            categoryLists.add(category);


                        }


                        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), categoryLists);
                        spinnerCategory.setAdapter(customAdapter);
                    }
                });
    }
}