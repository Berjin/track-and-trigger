package com.track.trackandtrigger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.track.trackandtrigger.Modal.ItemsModel;

public class ItemRecyclerviewAdapter extends FirebaseRecyclerAdapter<ItemsModel, ItemRecyclerviewAdapter.ViewHolder> {

    public ItemRecyclerviewAdapter(@NonNull FirebaseRecyclerOptions<ItemsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ItemsModel model) {
        holder.textItem.setText(model.getTopic());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item_details,parent,false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textItem;
        ImageView category_popup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textItem =itemView.findViewById(R.id.textItem);
            category_popup = itemView.findViewById(R.id.category_popup);
            category_popup.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
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
