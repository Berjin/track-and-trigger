package com.track.trackandtrigger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.track.trackandtrigger.Modal.RemindersModel;


public class ReminderRecyclerviewAdapter extends FirebaseRecyclerAdapter<RemindersModel, ReminderRecyclerviewAdapter.ViewHolder> {
    private String[] ReminderTitles = new String[100];
    private String[] ReminderTimes = new String[100];
    private int[] ReminderIds = new int[100];
    int position;
    DatabaseReference ref;

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
        ReminderIds[position] = model.getReminder_id();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView reminder_text;
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
                        position =getAdapterPosition();
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ref = FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE);
                        ref.child(uid).child("Reminders").child(ReminderTitles[position]).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                AlarmManager alarmManager = (AlarmManager)view.getContext().getSystemService(Context.ALARM_SERVICE);
                                Intent alarmCancelIntent = new Intent(view.getContext().getApplicationContext(),AlarmReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext().getApplicationContext(),ReminderIds[position],alarmCancelIntent,PendingIntent.FLAG_CANCEL_CURRENT);
                                Toast.makeText(view.getContext(), "deleted", Toast.LENGTH_SHORT).show();
                                alarmManager.cancel(pendingIntent);
                            }
                        });
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
