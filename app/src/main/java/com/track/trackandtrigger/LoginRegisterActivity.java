package com.track.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginRegisterActivity extends AppCompatActivity {

    private static final int AUTH_REQUEST_CODE = 1702;
    List<AuthUI.IdpConfig> providers;
    private static final String TAG = "LoginRegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, SplashScreenActivity.class));
            this.finish();
        }
    }
    public void handleLoginRegister(View view){
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(), //Email Builder
                new AuthUI.IdpConfig.FacebookBuilder().build(), // Facebook Builder
                new AuthUI.IdpConfig.GoogleBuilder().build() // Google builder
        );

        Intent intent = AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls("https://example.com","https://example.com")
//                .setLogo(R.drawable.logo)
                .setTheme(R.style.LoginTheme)
                .build();
        startActivityForResult(intent,AUTH_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AUTH_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode==RESULT_OK){
                //Get User
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //show email on toast
                Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                if(user.getMetadata().getCreationTimestamp()==user.getMetadata().getLastSignInTimestamp()){
                    Toast.makeText(this, "Welcome new user", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
                }
                if(user.isEmailVerified()){
                    Intent intent = new Intent(this, SplashScreenActivity.class);
                    startActivity(intent);
                    this.finish();
                }
                else{
                    Intent intent = new Intent(this, EmailVerifyActivity.class);
                    startActivity(intent);
                    this.finish();
                }

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
}

