package com.track.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.Query;
import com.track.trackandtrigger.Modal.DiaryModel;

public class Diary extends Fragment {

    View diaryinflator;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference notebookRef=db.collection("Notebook");

    private DiaryRecyclerviewAdapter adapter;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Diary() {
        // Required empty public constructor
    }

    public static Diary newInstance(String param1, String param2) {
        Diary fragment = new Diary();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         diaryinflator= inflater.inflate(R.layout.fragment_diary, container, false);
        ImageView add_btn_diary = diaryinflator.findViewById(R.id.add_btn_diary);
        add_btn_diary.setOnClickListener(v->{
            Intent intent = new Intent(getContext(),AddDiaryActivity.class);
            startActivity(intent);
        });
        setUpRecyclerView();

        return diaryinflator;
    }

    private void setUpRecyclerView() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView=diaryinflator.findViewById(R.id.diary_recycler_view);
        Query query=notebookRef.document(uid).collection("diary").orderBy("datetime",Query.Direction.DESCENDING);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirestoreRecyclerOptions<DiaryModel> options=new FirestoreRecyclerOptions.Builder<DiaryModel>()
                .setQuery(query, DiaryModel.class)
                .build();
        adapter=new DiaryRecyclerviewAdapter(options);


        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
                Toast.makeText(getContext(), "Diary entry deleted", Toast.LENGTH_SHORT).show();
            }

        }).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}