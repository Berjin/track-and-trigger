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
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.popup_edit:
                            //TODO reminder edit action
                            Toast.makeText(view.getContext(), "Edit", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.popup_delete:
                            //TODO reminder delete action
                           // AlarmManager alarmManager = (AlarmManager)view.getContext().getSystemService(Context.ALARM_SERVICE);
                           // Intent myIntent = new Intent(view.getContext(), AlarmReceiver.class);
                           // PendingIntent pendingIntent = PendingIntent.getBroadcast(
                             //       view.getContext(), 799958, myIntent, 0);                            // TODO add reminderId here and uncomment

                            //alarmManager.cancel(pendingIntent);
                            break;
                    }
                    return false;
                }
            });
        }
    }
}
