package com.track.trackandtrigger;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.track.trackandtrigger.Modal.ItemsModel;
import com.track.trackandtrigger.Modal.RemindersModel;
import com.track.trackandtrigger.Modal.UserInfoModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> categoryTitles = new ArrayList<>();
    private ArrayList<String> today_reminder_texts = new ArrayList<>();
    private ArrayList<String> today_reminder_times = new ArrayList<>();
    private ArrayList<String> tomorrow_reminder_texts = new ArrayList<>();
    private ArrayList<String> tomorrow_reminder_times = new ArrayList<>();

    ReminderRecyclerviewAdapter reminderRecyclerviewAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
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
    }

    @Override
    public void onStop() {
        super.onStop();
        reminderRecyclerviewAdapter.stopListening();
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
            Toast.makeText(getContext(), "Add Category", Toast.LENGTH_SHORT).show();
        });
        FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                snapshot.child("Items").getChildren().iterator().forEachRemaining((fruit) -> categoryTitles.add(fruit.getKey().trim()));
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView categoryRecyclerView = homeInflater.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
        CategoryRecyclerviewAdapter adapter = new CategoryRecyclerviewAdapter(getContext(), categoryTitles);
        categoryRecyclerView.setAdapter(adapter);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayoutManager todayReminderLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView todayReminderRecyclerView = homeInflater.findViewById(R.id.reminder_today_recycler_view);
        todayReminderRecyclerView.setLayoutManager(todayReminderLayoutManager);
        FirebaseRecyclerOptions<RemindersModel> options = new FirebaseRecyclerOptions.Builder<RemindersModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE).child(uid).child("Reminders").orderByChild("-datetime"), RemindersModel.class)
                .build();
        reminderRecyclerviewAdapter = new ReminderRecyclerviewAdapter(options);
        todayReminderRecyclerView.setAdapter(reminderRecyclerviewAdapter);
        return homeInflater;
    }
}

