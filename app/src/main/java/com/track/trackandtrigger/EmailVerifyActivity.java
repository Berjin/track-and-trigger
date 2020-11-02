package com.track.trackandtrigger;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerifyActivity extends AppCompatActivity {
    private static final String TAG = "EmailVerifyActivity";
    Button btn_email_verify;
    TextInputEditText edit_verification_code;
    Button btn_send_verification_email;
    TextInputLayout verify_code_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        // Assign Values to Views
        btn_email_verify = findViewById(R.id.btn_email_verify);
        btn_send_verification_email = findViewById(R.id.btn_send_verification_email);
        edit_verification_code = findViewById(R.id.edit_verification_code);
        verify_code_layout = findViewById(R.id.verify_code_layout);
        edit_verification_code.setVisibility(View.INVISIBLE);
        verify_code_layout.setVisibility(View.INVISIBLE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        //Send a verification email when clicking this button
        btn_send_verification_email.setOnClickListener((view)->{
            int code = 542121; // TODO generate a code to send to user
            sendVerificationEmail(email,code);
            btn_send_verification_email.setVisibility(View.INVISIBLE);
            verify_code_layout.setVisibility(View.VISIBLE);
            edit_verification_code.setVisibility(View.VISIBLE);
        });

        // verify the code typed by user with the one which we send
        btn_email_verify.setOnClickListener((view)-> {
            // TODO verify the code typed by user with the one which we send
            // TODO if the code is correct redirect to Main Activity else show a toast
            Toast.makeText(this, "" + edit_verification_code.getText().toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Verified/not verified", Toast.LENGTH_SHORT).show();
        });
//        if(user.getPhoneNumber()!=null){
//            phoneNo.setVisibility(View.INVISIBLE);
//            btn_verify_number.setVisibility(View.INVISIBLE);
//        }

    }

    //Send verification code
    private void sendVerificationEmail(String email, int code) {
        // TODO send the code to user's email
        Toast.makeText(this, "An email with code "+code+" will be send to "+email, Toast.LENGTH_SHORT).show();
    }


//    private void sendVerificationCode() {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNo.getText().toString(),
//                60,
//                TimeUnit.SECONDS,
//                this,
//                mCallbacks);
//    }
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            verificationCodeBySystem =s;
//        }
//
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//            String code = phoneAuthCredential.getSmsCode();
//            if(code!=null){
//                verifyCode(code);
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(EmailVerifyActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    private void verifyCode(String codeByUser) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem,codeByUser);
//        signInTheUserByCredentials(credential);
//    }
//
//    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.getCurrentUser().linkWithCredential(credential)
//                .addOnCompleteListener(this, task -> {
//                    if(task.isSuccessful()){
//                        Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        Toast.makeText(EmailVerifyActivity.this, ""+firebaseAuth.getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(EmailVerifyActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
