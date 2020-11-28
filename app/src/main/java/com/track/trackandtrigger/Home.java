package com.track.trackandtrigger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.track.trackandtrigger.Modal.ItemsModel;
import com.track.trackandtrigger.Modal.RemindersModel;
import com.track.trackandtrigger.Modal.UserInfoModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Home extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> categoryTitles = new ArrayList<>();
    private Set<String> categoryTitlesSet = new HashSet<>();
    ValueEventListener listener;
    DatabaseReference ref;
    RecyclerView todayReminderRecyclerView;
    RecyclerView categoryRecyclerView;

    ReminderRecyclerviewAdapter reminderRecyclerviewAdapter;
    CategoryRecyclerviewAdapter adapter;

    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        reminderRecyclerviewAdapter.startListening();
        ref.addValueEventListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        reminderRecyclerviewAdapter.stopListening();
        ref.removeEventListener(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeInflater = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView add_category_home = homeInflater.findViewById(R.id.add_category_home);
        TextView textUsername = homeInflater.findViewById(R.id.textUsername);
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        textUsername.setText(userName + ",");
        add_category_home.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),CategoryActivity.class);
            startActivity(intent);
        });

        ref = FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        snapshot.child("Items").getChildren().iterator().forEachRemaining(v -> categoryTitlesSet.add(v.getKey()));
                        for (String i : categoryTitlesSet) {
                            if (!categoryTitles.contains(i)) {
                                categoryTitles.add(i);
                            }
                        }
                        adapter = new CategoryRecyclerviewAdapter(getContext(), categoryTitles);
                        categoryRecyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(listener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView = homeInflater.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CategoryRecyclerviewAdapter(getContext(), categoryTitles);
        categoryRecyclerView.setAdapter(adapter);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayoutManager todayReminderLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        todayReminderRecyclerView = homeInflater.findViewById(R.id.reminder_today_recycler_view);
        todayReminderRecyclerView.setLayoutManager(todayReminderLayoutManager);
        FirebaseRecyclerOptions<RemindersModel> options = new FirebaseRecyclerOptions.Builder<RemindersModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE).child(uid).child("Reminders").orderByChild("-datetime").limitToFirst(5), RemindersModel.class)
                .build();
        reminderRecyclerviewAdapter = new ReminderRecyclerviewAdapter(options);
        todayReminderRecyclerView.setAdapter(reminderRecyclerviewAdapter);
        return homeInflater;
    }
}

