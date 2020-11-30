package com.track.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.track.trackandtrigger.Modal.ItemsModel;
import com.track.trackandtrigger.Modal.RemindersModel;

public class CategoryActivity extends AppCompatActivity {


    RecyclerView item_recycler_view;
    ItemRecyclerviewAdapter itemRecyclerViewAdapter;
    TextView textCategoryName;
    Query query;
    FirebaseRecyclerOptions<ItemsModel> options;
    String text,uid,categoryName;
    EditText item_search_edit;

    @Override
    public void onStart() {
        super.onStart();
        itemRecyclerViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        itemRecyclerViewAdapter.stopListening();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Intent intent = getIntent();
        categoryName = intent.getStringExtra("Category");

        textCategoryName = findViewById(R.id.textCategoryName);
        textCategoryName.setText(categoryName);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        item_recycler_view = findViewById(R.id.item_recycler_view);
        item_recycler_view.setLayoutManager(linearLayoutManager);
        ImageView item_search_btn = findViewById(R.id.item_search_btn);
        item_search_edit = findViewById(R.id.item_search_edit);
        item_search_btn.setOnClickListener(v -> {
            search();
        });
        item_search_edit.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId== EditorInfo.IME_ACTION_SEARCH){
                search();
                return true;
            }
            return false;
        });


        query = FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE).child(uid).child("Items").child(categoryName).orderByChild("topic");
        options = new FirebaseRecyclerOptions.Builder<ItemsModel>()
                .setQuery(query, ItemsModel.class)
                .build();
        itemRecyclerViewAdapter = new ItemRecyclerviewAdapter(options);
        item_recycler_view.setAdapter(itemRecyclerViewAdapter);
    }

    private void search() {
        text = item_search_edit.getText().toString();
        //Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ScrollView mainLayout = findViewById(R.id.mainLayout);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(),0);
        query = FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE).child(uid).child("Items").child(categoryName).orderByChild("topic").startAt(text).endAt(text+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<ItemsModel>()
                .setQuery(query, ItemsModel.class)
                .build();
        itemRecyclerViewAdapter = new ItemRecyclerviewAdapter(options);
        itemRecyclerViewAdapter.startListening();
        item_recycler_view.setAdapter(itemRecyclerViewAdapter);
    }
}