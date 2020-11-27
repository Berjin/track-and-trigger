package com.track.trackandtrigger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.track.trackandtrigger.Modal.RemindersModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reminders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reminders extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView allReminderRecyclerView;
    ReminderRecyclerviewAdapter allreminderRecyclerviewAdapter;
    private String mParam1;
    private String mParam2;

    public Reminders() {
        // Required empty public constructor
    }

    public static Reminders newInstance(String param1, String param2) {
        Reminders fragment = new Reminders();
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
        allreminderRecyclerviewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        allreminderRecyclerviewAdapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View remindersInflate = inflater.inflate(R.layout.fragment_reminders, container, false);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayoutManager todayReminderLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        allReminderRecyclerView = remindersInflate.findViewById(R.id.reminders_recycler_view);
        allReminderRecyclerView.setLayoutManager(todayReminderLayoutManager);
        FirebaseRecyclerOptions<RemindersModel> options = new FirebaseRecyclerOptions.Builder<RemindersModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE).child(uid).child("Reminders").orderByChild("-datetime"), RemindersModel.class)
                .build();
        allreminderRecyclerviewAdapter = new ReminderRecyclerviewAdapter(options);
        allReminderRecyclerView.setAdapter(allreminderRecyclerviewAdapter);
        return remindersInflate;
    }
}