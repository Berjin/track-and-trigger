package com.track.trackandtrigger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;


public class SplashScreenActivity extends AppCompatActivity {
    private static final int LOGIN_REQUEST_CODE = 1702;
    private static final String TAG = "Splash Screen Activity";
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onStart() {
        super.onStart();
        delaySplashScreen();
    }

    @Override
    protected void onStop() {
        if(firebaseAuth!=null && listener!=null){
            firebaseAuth.removeAuthStateListener(listener);
        }
        super.onStop();
    }

    private void delaySplashScreen() {
        Completable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
    }

    private void init() {
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );
        firebaseAuth = FirebaseAuth.getInstance();
        listener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user != null){
                Toast.makeText(this,"Welcome: "+user.getUid(),Toast.LENGTH_SHORT).show();
            }else{
                showLoginLayout();
            }
        };
    }

    private void showLoginLayout() {
        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.activity_login)
                .setEmailButtonId(R.id.btn_email_sign_in)
                .setGoogleButtonId(R.id.btn_google_sign_in)
                .setFacebookButtonId(R.id.btn_fb_sign_in)
                .build();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAuthMethodPickerLayout(authMethodPickerLayout)
                        .setTheme(R.style.LoginTheme)
                        .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build()
                , LOGIN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == Activity.RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "YESSSS", Toast.LENGTH_SHORT).show();
            }
            else {
                if(response==null){
                    Log.d(TAG,"OnActivityResult: User cancelled sign in");
                }
                else{
                    Log.d(TAG,"OnActivityResult: "+response.getError());
                }
            }
        }
    }
    //    Button btn_sign_out;
//    private static final String TAG = "MainActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        btn_sign_out = findViewById(R.id.btn_sign_out);
//        btn_sign_out.setEnabled(true);
//        btn_sign_out.setOnClickListener((view)-> AuthUI.getInstance()
//                .signOut(SplashScreenActivity.this));
//    }
//
//    private void startLoginActivity() {
//        Intent intent = new Intent(this,LoginRegisterActivity.class);
//        startActivity(intent);
//        this.finish();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseAuth.getInstance().addAuthStateListener(this);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        FirebaseAuth.getInstance().removeAuthStateListener(this);
//    }
//
//    @Override
//    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//        if(firebaseAuth.getCurrentUser() == null){
//            startLoginActivity();
//            return;
//        }else{
//            if(!firebaseAuth.getCurrentUser().isEmailVerified() || firebaseAuth.getCurrentUser().getPhoneNumber()==null){
//                Intent intent = new Intent(this, EmailVerifyActivity.class);
//                startActivity(intent);
//                this.finish();
//            }
//        }
//        firebaseAuth.getCurrentUser().getIdToken(true)
//                .addOnSuccessListener((getTokenResult)-> Log.d(TAG, "onSuccess: "+getTokenResult.getToken()));
//    }


}