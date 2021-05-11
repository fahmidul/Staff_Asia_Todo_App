package com.customerkoi.todoapp.activity;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.customerkoi.todoapp.helpers.InternetChecker;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ocufoxtech.firebasetodoruia.R;

import java.util.Collections;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 101;
    public static String TAG = "firebase_todo_app";
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.linGmailLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetChecker.isNetworkAvailable(getApplicationContext())) {
                    if (auth.getCurrentUser() == null) {

                        signIn();
                    } else {

                        launchActivity(auth.getCurrentUser());
                    }
                }else {
                    Toast.makeText(getApplicationContext(), getString(R.string.check_your_internet_connection), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN );
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 9999){
//            if (resultCode == RESULT_OK){
//                launchActivity(auth.getCurrentUser());
//            }else{
//                Log.e("LoginActivity", "onActivityResult: ", IdpResponse.fromResultIntent(data).getError());
//                Toast.makeText(this, "Login Failed. Try Again", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void launchActivity(FirebaseUser currentUser) {
        Intent i = new Intent(this,HomeActivity.class);
        i.putExtra("name",currentUser.getDisplayName());
        i.putExtra("email",currentUser.getEmail());
        startActivity(i);
        finish();
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = auth.getCurrentUser();

//                            dbRef.child(user.getUid()).child("tasks")
//                                    .setValue("Project Submit", completionListener);
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

//                            finish();

                            launchActivity(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                        }
                    }
                });
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
