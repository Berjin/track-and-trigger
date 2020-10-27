package com.track.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EmailVerifyActivity extends AppCompatActivity {
    private String verificationId;
    private static final String TAG = "EmailVerifyActivity";
    Button btn_email_verify;
    Button btn_verify_signout;
    Button btn_verify_number;
    EditText phoneNo;
    TextView email_verify_text;
    String verificationCodeBySystem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);
        btn_email_verify = findViewById(R.id.btn_email_verify);
        btn_verify_signout = findViewById(R.id.btn_verify_signout);
        btn_verify_number = findViewById(R.id.btn_verify_number);
        email_verify_text = findViewById(R.id.email_verify_text);
        phoneNo = findViewById(R.id.phoneNo);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.isEmailVerified()){
            btn_email_verify.setVisibility(View.INVISIBLE);
            email_verify_text.setVisibility(View.INVISIBLE);
        }
        if(user.getPhoneNumber()!=null){
            phoneNo.setVisibility(View.INVISIBLE);
            btn_verify_number.setVisibility(View.INVISIBLE);
        }
        btn_email_verify.setOnClickListener((view)-> user.sendEmailVerification()
                .addOnCompleteListener((task)->{
                    findViewById(R.id.btn_email_verify).setEnabled(false);
                    if(task.isSuccessful()){
                        Toast.makeText(this, "Verification Email sent to "+user.getEmail(), Toast.LENGTH_SHORT).show();
                        AuthUI.getInstance()
                                .signOut(this);
                        startActivity(new Intent(this,LoginRegisterActivity.class));
                        this.finish();
                    }
                    else{
                        Log.d(TAG, "onCreate: "+task.getException());
                        Toast.makeText(this, "Failed to verify Email", Toast.LENGTH_SHORT).show();
                    }
                }));
        btn_verify_number.setOnClickListener((view)-> sendVerificationCode());
        btn_verify_signout.setOnClickListener((view)->{
            AuthUI.getInstance()
                    .signOut(this);
            startActivity(new Intent(this,LoginRegisterActivity.class));
            this.finish();
        });

    }

    private void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem =s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(EmailVerifyActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(EmailVerifyActivity.this, ""+firebaseAuth.getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(EmailVerifyActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
