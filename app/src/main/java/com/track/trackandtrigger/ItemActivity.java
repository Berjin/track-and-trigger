package com.track.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent intent =getIntent();
        String topic = intent.getStringExtra("Topic");
        Toast.makeText(this, topic, Toast.LENGTH_SHORT).show();
    }
}