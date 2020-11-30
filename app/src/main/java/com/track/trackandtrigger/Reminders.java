package com.track.trackandtrigger;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.track.trackandtrigger.Modal.RemindersModel;

public class Reminders extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView allReminderRecyclerView;
    ReminderRecyclerviewAdapter allreminderRecyclerviewAdapter;
    String uid,text;
    FirebaseRecyclerOptions<RemindersModel> options;
    Query query;
    EditText allRemindersSearch;

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
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayoutManager todayReminderLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        allReminderRecyclerView = remindersInflate.findViewById(R.id.reminders_recycler_view);
        ImageView allRemindersSearchBtn = remindersInflate.findViewById(R.id.allReminderSearchBtn);
        allRemindersSearch = remindersInflate.findViewById(R.id.allRemindersSearch);
        allRemindersSearchBtn.setOnClickListener(v -> {
            search();
        });
        allRemindersSearch.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId== EditorInfo.IME_ACTION_SEARCH){
                search();
                return true;
            }
            return false;
        });
        query = FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE).child(uid).child("Reminders").orderByChild("title");
        allReminderRecyclerView.setLayoutManager(todayReminderLayoutManager);
        options = new FirebaseRecyclerOptions.Builder<RemindersModel>()
                .setQuery(query, RemindersModel.class)
                .build();
        allreminderRecyclerviewAdapter = new ReminderRecyclerviewAdapter(options);
        allReminderRecyclerView.setAdapter(allreminderRecyclerviewAdapter);
        return remindersInflate;
    }

    private void search() {
        text = allRemindersSearch.getText().toString();
        //Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(),0);
        query = FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE).child(uid).child("Reminders").orderByChild("title").startAt(text).endAt(text+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<RemindersModel>()
                .setQuery(query, RemindersModel.class)
                .build();
        allreminderRecyclerviewAdapter = new ReminderRecyclerviewAdapter(options);
        allreminderRecyclerviewAdapter.startListening();
        allReminderRecyclerView.setAdapter(allreminderRecyclerviewAdapter);
    }
}