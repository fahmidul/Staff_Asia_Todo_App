package com.customerkoi.todoapp.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ocufoxtech.firebasetodoruia.R;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    EditText etTodo;
    Button btnAddTodo;

    FirebaseFirestore db;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        db = FirebaseFirestore.getInstance();

        etTodo = findViewById(R.id.etTodo);
        btnAddTodo = findViewById(R.id.btnAddTodo);

        id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");

        if (name != null) {
            etTodo.setText(name);
            btnAddTodo.setText("Update Todo");
        }

        btnAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoName = etTodo.getText().toString().trim();
                if (todoName.isEmpty()) {
                    Toast.makeText(AddActivity.this, "Task Name cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (id == null) {
                    Log.d("sjdskjdska","in_1");
                    db.collection("todos")
                            .document()
                            .set(Collections.singletonMap("name", todoName))
                            .addOnCompleteListener(AddActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(AddActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                } else {
                    Log.d("sjdskjdska","in_2");
                db.collection("todos")
                        .document(id)
                        .set(Collections.singletonMap("name", todoName))
                        .addOnCompleteListener(AddActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }

                    FirebaseUser testUser = FirebaseAuth.getInstance().getCurrentUser(); //getting the current logged in users id
                    String userUid = testUser.getUid();
                    Map<String, Object> toDo=new HashMap<>();
                    toDo.put("id",userUid);
                    toDo.put("title",etTodo.getText().toString());
                    toDo.put("description","hiiii");

                    db.collection("todos").document().set(toDo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
        });

    }
}