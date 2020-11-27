package com.track.trackandtrigger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.track.trackandtrigger.Modal.RemindersModel;

import java.util.ArrayList;


public class ReminderRecyclerviewAdapter extends FirebaseRecyclerAdapter<RemindersModel, ReminderRecyclerviewAdapter.ViewHolder> {
    private String[] ReminderTitles = new String[100];
    private String[] ReminderTimes = new String[100];

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
        ReminderTitles[position] = model.getTitle();
        ReminderTimes[position] = model.getDatetime();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox reminder_text;
        TextView reminder_time;
        ImageView reminder_popup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reminder_text = itemView.findViewById(R.id.reminder_text);
            reminder_time =itemView.findViewById(R.id.reminder_time);
            reminder_popup = itemView.findViewById(R.id.reminder_popup);
            reminder_popup.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }
        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.reminder_popup_menu);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.popup_edit:
                        //TODO reminder edit action
                        Toast.makeText(view.getContext(), "Edit", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.popup_delete:
                        //TODO reminder delete action
                        Toast.makeText(view.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.popup_share:
                        int position =getAdapterPosition();
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "Reminder:\n"+ReminderTitles[position]+" at "+ReminderTimes[position];
                        sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                        view.getContext().startActivity(Intent.createChooser(sharingIntent,"Share via"));
                        break;
                }
                return false;
            });
        }
    }
}
