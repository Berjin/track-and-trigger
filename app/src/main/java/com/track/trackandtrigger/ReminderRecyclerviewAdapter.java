package com.track.trackandtrigger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.track.trackandtrigger.Modal.RemindersModel;


public class ReminderRecyclerviewAdapter extends FirebaseRecyclerAdapter<RemindersModel, ReminderRecyclerviewAdapter.ViewHolder> {

    public ReminderRecyclerviewAdapter(@NonNull FirebaseRecyclerOptions<RemindersModel> options) {
        super(options);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reminder_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull RemindersModel model) {
        holder.reminder_text.setText(model.getTitle());
        holder.reminder_time.setText(model.getDatetime());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox reminder_text;
        TextView reminder_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reminder_text = itemView.findViewById(R.id.reminder_text);
            reminder_time =itemView.findViewById(R.id.reminder_time);
        }
    }
}
