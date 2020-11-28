package com.track.trackandtrigger;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.track.trackandtrigger.Modal.ItemsModel;

import java.util.ArrayList;

public class ItemRecyclerviewAdapter extends FirebaseRecyclerAdapter<ItemsModel, ItemRecyclerviewAdapter.ViewHolder> {

    private ArrayList<String> ItemsArray = new ArrayList<>();
    private ArrayList<String> ItemsCount = new ArrayList<>();
    public ItemRecyclerviewAdapter(@NonNull FirebaseRecyclerOptions<ItemsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ItemsModel model) {
        holder.textItem.setText(model.getTopic());
        holder.item_count_text.setText(model.getItemCount());
        ItemsArray.add(model.getTopic());
        ItemsCount.add(model.getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item_details,parent,false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textItem,item_count_text;
        ImageView category_popup;
        CardView item_card_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textItem =itemView.findViewById(R.id.textItem);
            item_count_text = itemView.findViewById(R.id.item_count_text);
            category_popup = itemView.findViewById(R.id.category_popup);
            item_card_view = itemView.findViewById(R.id.item_card_view);
            category_popup.setOnClickListener(this);
            item_card_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.category_popup:
                    showPopupMenu(v);
                    break;
                case R.id.item_card_view:
                    int position = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(),ItemActivity.class);
                    intent.putExtra("Topic",ItemsArray.get(position));
                    intent.putExtra("count",ItemsCount.get(position));
                    v.getContext().startActivity(intent);
            }
        }
        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.item_popup_menu);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.item_popup_edit:
                        //TODO Item edit action
                        Toast.makeText(view.getContext(), "Edit", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_popup_delete:
                        //TODO Item delete action
                        Toast.makeText(view.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            });
        }
    }
}
