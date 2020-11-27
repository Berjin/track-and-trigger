package com.track.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.track.trackandtrigger.Modal.ItemsModel;
import com.track.trackandtrigger.Modal.RemindersModel;

public class CategoryActivity extends AppCompatActivity {


    RecyclerView item_recycler_view;
    ItemRecyclerviewAdapter itemRecyclerViewAdapter;
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
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        item_recycler_view = findViewById(R.id.item_recycler_view);
        item_recycler_view.setLayoutManager(linearLayoutManager);
        FirebaseRecyclerOptions<ItemsModel> options = new FirebaseRecyclerOptions.Builder<ItemsModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE).child(uid).child("Items").child("Groceries").orderByChild("topic"), ItemsModel.class)
                .build();
        itemRecyclerViewAdapter = new ItemRecyclerviewAdapter(options);
        item_recycler_view.setAdapter(itemRecyclerViewAdapter);
    }
}