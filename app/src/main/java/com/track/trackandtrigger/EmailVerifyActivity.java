package com.track.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

import static java.lang.Integer.parseInt;


public class EmailVerifyActivity extends AppCompatActivity {
    private static final String TAG = "EmailVerifyActivity";
    Button btn_email_verify;
    TextInputEditText edit_verification_code;
    Button btn_send_verification_email;
    TextInputLayout verify_code_layout;
    String user = "trackntrigger@gmail.com";
    String password = "fcttlzfnbtlhlxuk";
    String email;
    int code;

    private FirebaseDatabase database;
    private DatabaseReference userInfoRef;


    GMailSender sender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_email_verify);
        btn_email_verify = findViewById(R.id.btn_email_verify);
        btn_send_verification_email = findViewById(R.id.btn_send_verification_email);
        edit_verification_code = findViewById(R.id.edit_verification_code);
        verify_code_layout = findViewById(R.id.verify_code_layout);
        edit_verification_code.setVisibility(View.INVISIBLE);
        verify_code_layout.setVisibility(View.INVISIBLE);
        btn_email_verify.setVisibility(View.INVISIBLE);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        //Send a verification email when clicking this button
        btn_send_verification_email.setOnClickListener((view)->{
             code =new Random().nextInt(900000) + 100000;
            sendVerificationEmail(email,code);

            btn_send_verification_email.setVisibility(View.INVISIBLE);
            verify_code_layout.setVisibility(View.VISIBLE);
            edit_verification_code.setVisibility(View.VISIBLE);
            btn_email_verify.setVisibility(View.VISIBLE);
        });

        // verify the code typed by user with the one which we send
        btn_email_verify.setOnClickListener((view)-> {
                    if(TextUtils.isEmpty(edit_verification_code.getText().toString())){
                        Toast.makeText(this, "Please enter verification code", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (parseInt(edit_verification_code.getText().toString()) == code) {
                            Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show();
                            HashMap hashMap = new HashMap();
                            hashMap.put("isEmailVerified", true);
                            database = FirebaseDatabase.getInstance();
                            userInfoRef = database.getReference(Common.USER_INFO_REFERENCE);
                            String uid = user.getUid();
                            userInfoRef.child(uid).updateChildren(hashMap).addOnSuccessListener(o -> {
                                if(TextUtils.isEmpty(user.getPhoneNumber())){
                                    startActivity(new Intent(this, PhoneVerifyActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(this, MainActivity.class));
                                    finish();
                                }
                            });
                        } else {
                            Toast.makeText(this, "not verified", Toast.LENGTH_SHORT).show();
                        }
                    }


        });

    }

    //Send verification code
    private void sendVerificationEmail(String email, int code) {
        //
        Toast.makeText(this, "An email with code  will be send to "+email, Toast.LENGTH_SHORT).show();
        sender=new GMailSender(user,password);

        MyAsyncClass myasync = new MyAsyncClass();
        myasync.execute();
    }


    class MyAsyncClass extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(EmailVerifyActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();

        }

        @Override

        protected Void doInBackground(Void... mApi) {
            try {

                // Add subject, Body, your mail Id, and receiver mail Id.
                sender.sendMail("Please verify your email", "To verify your email address, please use the following One Time Password(OTP):\n"+ code, "trackntrigger@gmail.com", email);
                Log.d("send", "done");
            } catch (Exception ex) {
                Log.d("exceptionsending", ex.toString());
            }
            return null;
        }

        @Override

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            pDialog.cancel();

            Toast.makeText(EmailVerifyActivity.this, "Email has been sent", Toast.LENGTH_SHORT).show();


        }
    }
}
