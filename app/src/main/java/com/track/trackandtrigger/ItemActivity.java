package com.track.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity {
    TextView ItemNameText,ItemCountText,textItemName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent intent =getIntent();
        String topic = intent.getStringExtra("Topic");
        String count = intent.getStringExtra("count");
        ItemNameText = findViewById(R.id.ItemNameText);
        ItemCountText = findViewById(R.id.ItemCountText);
        textItemName =findViewById(R.id.textItemName);
        ItemNameText.setText(topic);
        ItemCountText.setText(count);
        textItemName.setText(topic);
    }
}