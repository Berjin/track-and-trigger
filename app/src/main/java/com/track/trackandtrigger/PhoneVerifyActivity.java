package com.track.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.track.trackandtrigger.Modal.UserInfoModel;

import java.util.concurrent.TimeUnit;


public class PhoneVerifyActivity extends AppCompatActivity {
    Button btn_phone_verify;
    TextInputEditText edit_verify_code;
    Button btn_send_verify_code,btn_sign_out;
    TextInputLayout verify_code_text;
    FirebaseAuth mAuth;
    String verificationCodeBySystem;
    private FirebaseDatabase database;
    private DatabaseReference userInfoRef;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        btn_sign_out = findViewById(R.id.btn_sign_out);
        btn_phone_verify = findViewById(R.id.btn_phone_verify);
        btn_send_verify_code = findViewById(R.id.btn_send_verify_code);
        edit_verify_code = findViewById(R.id.edit_verify_code);
        verify_code_text = findViewById(R.id.verify_code_text);
        edit_verify_code.setVisibility(View.INVISIBLE);
        verify_code_text.setVisibility(View.INVISIBLE);
        btn_phone_verify.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        btn_send_verify_code.setOnClickListener(v -> {
            database = FirebaseDatabase.getInstance();
            userInfoRef = database.getReference(Common.USER_INFO_REFERENCE);
            userInfoRef
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                UserInfoModel model = snapshot.getValue(UserInfoModel.class);
                                sendVerificationCode(model.phoneNumber);
                            }
                            else {
                                Toast.makeText(PhoneVerifyActivity.this, "User not registered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(PhoneVerifyActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            btn_send_verify_code.setVisibility(View.INVISIBLE);
            verify_code_text.setVisibility(View.VISIBLE);
            edit_verify_code.setVisibility(View.VISIBLE);
            btn_phone_verify.setVisibility(View.VISIBLE);
        });
        btn_sign_out.setOnClickListener(v -> {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        startActivity(new Intent(this,SplashScreenActivity.class));
                        this.finish();
                    });
        });
        btn_phone_verify.setOnClickListener(v -> {
            if(TextUtils.isEmpty(edit_verify_code.getText().toString())){
                Toast.makeText(this, "Please enter verification code", Toast.LENGTH_SHORT).show();
            }
            else {
                String edit_code = edit_verify_code.getText().toString();
                verifyCode(edit_code);
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)// OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PhoneVerifyActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem =s;
        }


    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem,codeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(PhoneVerifyActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}