package com.customerkoi.todoapp.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.customerkoi.todoapp.activity.AddActivity;
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


public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    EditText edtCategory;
    FirebaseFirestore firebaseFirestore;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        firebaseFirestore = FirebaseFirestore.getInstance();
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        edtCategory = (EditText) findViewById(R.id.edtCategory);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:

                FirebaseUser testUser = FirebaseAuth.getInstance().getCurrentUser(); //getting the current logged in users id
                String userUid = testUser.getUid();
                Map<String, Object> toDo = new HashMap<>();
                toDo.put("id", userUid);
                toDo.put("category_name", edtCategory.getText().toString());

                firebaseFirestore.collection("todo_category").document().set(toDo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(c, "Category Successfully Added", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });


                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
