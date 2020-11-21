package com.track.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;


import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.track.trackandtrigger.Modal.UserInfoModel;

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
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseDatabase database;
    private DatabaseReference userInfoRef;

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

        database = FirebaseDatabase.getInstance();
        userInfoRef = database.getReference(Common.USER_INFO_REFERENCE);
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );
        firebaseAuth = FirebaseAuth.getInstance();
        listener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user != null){
                checkUserInFirebase();
            }else{
                showLoginLayout();
            }
        };
    }

    private void checkUserInFirebase() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userInfoRef
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            UserInfoModel model = snapshot.getValue(UserInfoModel.class);
                            if (model.isEmailVerified) {
                                if(TextUtils.isEmpty(user.getPhoneNumber())){
                                    startActivity(new Intent(getApplicationContext(), PhoneVerifyActivity.class));
                                    finish();
                                } else {
                                    goToHomeActivity(model);
                                }
                            } else {
                                verifyEmail();
                            }
                        }
                        else {
                            showRegisterLayout();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SplashScreenActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void goToHomeActivity(UserInfoModel model) {
        Common.currentUser = model;
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void showRegisterLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MainTheme);
        builder.setCancelable(false);
        View itemView = LayoutInflater.from(this).inflate(R.layout.activity_register,null);
        TextInputEditText edit_first_name = itemView.findViewById(R.id.first_name);
        TextInputLayout firstNameLayout = itemView.findViewById(R.id.first_name_layout);
        TextInputEditText edit_last_name = itemView.findViewById(R.id.last_name);
        TextInputLayout lastNameLayout = itemView.findViewById(R.id.last_name_layout);
        TextInputEditText edit_user_name = itemView.findViewById(R.id.edit_user_name);
        TextInputLayout usernameLayout = itemView.findViewById(R.id.username_layout);
        TextInputEditText edit_phone_number = itemView.findViewById(R.id.edit_phone_number);
        TextInputLayout phoneNumberLayout = itemView.findViewById(R.id.phone_number_layout);
        AutoCompleteTextView edit_profession = itemView.findViewById(R.id.edit_profession);
        TextInputLayout textInputLayout = itemView.findViewById(R.id.text_input_layout);
        String[] items = new String[]{
          "Student",
          "Home Maker",
          "Teacher",
          "Others"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                SplashScreenActivity.this,
                R.layout.dropdown_profession,
                items
        );
        edit_profession.setAdapter(adapter);
        Button btn_continue = itemView.findViewById(R.id.btn_register);
        Button btn_sign_out = itemView.findViewById(R.id.btn_sign_out);

        //SetData
        if(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()!=null && !TextUtils.isDigitsOnly(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())){
            edit_phone_number.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }
        //View
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();
        //Event
        btn_sign_out.setOnClickListener(v -> {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        startActivity(new Intent(this,SplashScreenActivity.class));
                        this.finish();
                    });
        });
        btn_continue.setOnClickListener(v -> {
            if(TextUtils.isDigitsOnly(edit_first_name.getText().toString().trim())){
                firstNameLayout.setHelperText("*Enter first name");
                firstNameLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                firstNameLayout.setHelperTextEnabled(true);
            }
            else if(TextUtils.isDigitsOnly(edit_last_name.getText().toString().trim())){
                lastNameLayout.setHelperText("*Enter last name");
                lastNameLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                lastNameLayout.setHelperTextEnabled(true);
                firstNameLayout.setHelperTextEnabled(false);
            }
            else if(TextUtils.isDigitsOnly(edit_user_name.getText().toString().trim())){
                usernameLayout.setHelperText("*Enter user name");
                usernameLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                usernameLayout.setHelperTextEnabled(true);
                lastNameLayout.setHelperTextEnabled(false);
                firstNameLayout.setHelperTextEnabled(false);
            }
            else if(TextUtils.isEmpty(edit_phone_number.getText().toString().trim())|| edit_phone_number.getText().toString().length()!=10){
                phoneNumberLayout.setHelperText("*Enter 10 digit phone number");
                phoneNumberLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                phoneNumberLayout.setHelperTextEnabled(true);
                usernameLayout.setHelperTextEnabled(false);
                lastNameLayout.setHelperTextEnabled(false);
                firstNameLayout.setHelperTextEnabled(false);
            }
            else if(TextUtils.isDigitsOnly(edit_profession.getText().toString().trim()) || !Arrays.asList(items).contains(edit_profession.getText().toString().trim())){
                textInputLayout.setHelperText("*Select profession from dropdown");
                textInputLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                textInputLayout.setHelperTextEnabled(true);
                phoneNumberLayout.setHelperTextEnabled(false);
                usernameLayout.setHelperTextEnabled(false);
                lastNameLayout.setHelperTextEnabled(false);
                firstNameLayout.setHelperTextEnabled(false);
                edit_profession.setText("");
            }
            else{
                UserInfoModel model = new UserInfoModel();
                model.firstName = edit_first_name.getText().toString().trim();
                model.lastName = edit_last_name.getText().toString().trim();
                model.phoneNumber = edit_phone_number.getText().toString().trim();
                model.userName = edit_user_name.getText().toString().trim();
                model.profession = edit_profession.getText().toString().trim();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                userInfoRef.child(uid)
                        .setValue(model)
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .addOnSuccessListener((e)->{
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            if (model.isEmailVerified) {
                                Toast.makeText(this, "Your email is verified", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                verifyEmail();
                            }

                        });
            }

        });
    }

    private void verifyEmail() {
        Intent intent = new Intent(this, EmailVerifyActivity.class);
        startActivity(intent);
        this.finish();
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