package com.track.trackandtrigger;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.track.trackandtrigger.Modal.DiaryModel;


public class DiaryRecyclerviewAdapter extends FirebaseRecyclerAdapter<DiaryModel,DiaryRecyclerviewAdapter.DiaryHolder> {

    public DiaryRecyclerviewAdapter(@NonNull FirestoreRecyclerOptions<DiaryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DiaryHolder holder, int position, @NonNull DiaryModel model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewDescription.setText(model.getDescription());

    }

    @NonNull
    @Override
    public DiaryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_diary_item,
                parent,false);

        return new DiaryHolder(v);

    }

    class DiaryHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle;
        TextView textViewDescription;

        public DiaryHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.diary_text);
            textViewDescription=itemView.findViewById(R.id.diary_content);

        }
    }
}
