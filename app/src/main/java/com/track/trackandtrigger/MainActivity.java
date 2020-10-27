package com.track.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;


import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    Button btn_sign_out;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_sign_out = findViewById(R.id.btn_sign_out);
        btn_sign_out.setEnabled(true);
        btn_sign_out.setOnClickListener((view)-> AuthUI.getInstance()
                .signOut(MainActivity.this));
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this,LoginRegisterActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser() == null){
            startLoginActivity();
            return;
        }else{
            if(!firebaseAuth.getCurrentUser().isEmailVerified() || firebaseAuth.getCurrentUser().getPhoneNumber()==null){
                Intent intent = new Intent(this, EmailVerifyActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
        firebaseAuth.getCurrentUser().getIdToken(true)
                .addOnSuccessListener((getTokenResult)-> Log.d(TAG, "onSuccess: "+getTokenResult.getToken()));
    }


}