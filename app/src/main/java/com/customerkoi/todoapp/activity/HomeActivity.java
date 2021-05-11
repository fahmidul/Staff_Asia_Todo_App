package com.customerkoi.todoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customerkoi.todoapp.adapters.TodoAdapter;
import com.customerkoi.todoapp.models.Todo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ocufoxtech.firebasetodoruia.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static String TAG = "firebase_todo_app";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore;
    private GoogleSignInClient googleSignInClient;
    List<Todo> todoArrayList;

    //    Fa btnAdd;
    RecyclerView recyclerViewTodoList;
    FloatingActionButton floatingActionButton;

    TodoAdapter adapter;

    Toolbar toolBar;
    FirebaseUser firebaseUser;
    String firebaseUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {

            startActivity(new Intent(this, LoginActivity.class));

        } else {



            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            firebaseUserId = firebaseUser.getUid();
            init();

        }


    }

    public void init() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        todoArrayList = new ArrayList<>();
//        firebaseFirestore = FirebaseFirestore.getInstance();

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        recyclerViewTodoList = (RecyclerView) findViewById(R.id.recyclerViewTodoList);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TodoItemAddingActivity.class));

            }
        });
        adapter = new TodoAdapter(todoArrayList, this, new TodoAdapter.OnButtonClickListener() {
            @Override
            public void onUpdateClicked(Todo todo) {
                Intent i = new Intent(HomeActivity.this, TodoItemAddingActivity.class);
                i.putExtra("id", todo.getId());
                i.putExtra("title", todo.getTitle());
                i.putExtra("description", todo.getDescription());

                i.putExtra("category_id", todo.getDescription());
                i.putExtra("category_name", todo.getDescription());
                i.putExtra("status", todo.getStatus());
                startActivity(i);
            }

            @Override
            public void onDeleteClicked(final Todo todo) {
                firebaseFirestore.collection("todos").
                        document(todo.getId())
                        .delete()
                        .addOnCompleteListener(HomeActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(HomeActivity.this, "Delete Successful", Toast.LENGTH_SHORT).show();
                                todoArrayList.remove(todo);
                                adapter.notifyDataSetChanged();
                            }
                        });
            }

            @Override
            public void onCompleteClicked(Todo todo) {

                todo.setStatus("1");

                firebaseFirestore.collection("todos").document(firebaseUserId).set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(HomeActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onDetailsClicked(Todo todo) {
                Intent i = new Intent(HomeActivity.this, TodoItemDetailsActivity.class);
                i.putExtra("title", todo.getTitle());
                i.putExtra("description", todo.getDescription());
                i.putExtra("category", todo.getCategory_name());
                i.putExtra("status", todo.getStatus());

                startActivity(i);
            }
        });
        recyclerViewTodoList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTodoList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getting unique user id
        firebaseFirestore.collection("todos").whereEqualTo("id", firebaseUserId).get()
                .addOnCompleteListener(HomeActivity.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        todoArrayList.clear();
                        for (DocumentSnapshot doc : task.getResult()) {
                            Todo todo = doc.toObject(Todo.class);
                            todo.setId(doc.getId());
                            todoArrayList.add(todo);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.log_out) {
            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseAuth.getInstance().signOut(); // very important if you are using firebase.
                        Toast.makeText(getApplicationContext(), "Logout successful!", Toast.LENGTH_LONG).show();
                        Intent login_intent = new Intent(getApplicationContext(), LoginActivity.class);
                        login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // clear previous task (optional)
                        startActivity(login_intent);
                    }
                }
            });
        }
        if (item.getItemId() == R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


}
