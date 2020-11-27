package com.track.trackandtrigger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.track.trackandtrigger.Modal.ItemsModel;
import com.track.trackandtrigger.Modal.RemindersModel;

import java.text.BreakIterator;
import java.util.ArrayList;

public class ItemRecyclerviewAdapter extends FirebaseRecyclerAdapter<ItemsModel, ItemRecyclerviewAdapter.ViewHolder> {

    public ReminderRecyclerviewAdapter(@NonNull FirebaseRecyclerOptions<ItemsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ItemsModel model) {
        holder.textItem.setText(model.getTitle());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item_details,parent,false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public BreakIterator textItem;
        CheckBox textItem;
    }
}
