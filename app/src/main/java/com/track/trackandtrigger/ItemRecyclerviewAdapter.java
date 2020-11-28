package com.track.trackandtrigger;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.track.trackandtrigger.Modal.ItemsModel;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class ItemRecyclerviewAdapter extends FirebaseRecyclerAdapter<ItemsModel, ItemRecyclerviewAdapter.ViewHolder> {

    private ArrayList<String> ItemsArray = new ArrayList<>();
    private ArrayList<String> ItemsCount = new ArrayList<>();
    private String[] ItemsCategory = new String[100];
    int position;
    DatabaseReference ref;
    public ItemRecyclerviewAdapter(@NonNull FirebaseRecyclerOptions<ItemsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ItemsModel model) {
        holder.textItem.setText(model.getTopic());
        holder.item_count_text.setText(model.getItemCount());
        ItemsArray.add(model.getTopic());
        ItemsCount.add(model.getItemCount());
        ItemsCategory[position]=model.getCategory();
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
                    position = getAdapterPosition();
                    showPopupMenu(v);
                    break;
                case R.id.item_card_view:
                    position = getAdapterPosition();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(),R.style.AddItemDialogTheme);
                        View itemView = LayoutInflater.from(view.getContext()).inflate(R.layout.item_count_dialog,null);
                        final int[] count = {0};
                        ImageView decrease_edit_count = itemView.findViewById(R.id.decrease_edit_count);
                        ImageView increase_edit_count = itemView.findViewById(R.id.increase_edit_count);
                        TextInputEditText item_edit_count = itemView.findViewById(R.id.item_edit_count);
                        Button btn_edit_save =itemView.findViewById(R.id.btn_edit_save);
                        builder.setView(itemView);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Toast.makeText(view.getContext(), ItemsCount.get(position), Toast.LENGTH_SHORT).show();
                        item_edit_count.setText(ItemsCount.get(position));
                        increase_edit_count.setOnClickListener(v1 -> {
                            count[0] = parseInt(item_edit_count.getText().toString().trim());
                            count[0]++;
                            item_edit_count.setText(""+count[0]);
                        });
                        decrease_edit_count.setOnClickListener(v1 -> {
                            count[0] = parseInt(item_edit_count.getText().toString().trim());
                            if(count[0]>0){
                                count[0]--;
                            }
                            item_edit_count.setText(""+count[0]);
                        });
                        btn_edit_save.setOnClickListener(v12 -> {
                            ItemsModel model = new ItemsModel();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference userInfoRef = database.getReference(Common.USER_INFO_REFERENCE);
                            String category = ItemsCategory[position];
                            String topic = ItemsArray.get(position);
                            model.topic = ItemsArray.get(position);
                            model.itemCount = item_edit_count.getText().toString().trim();
                            model.category = ItemsCategory[position];
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            userInfoRef.child(uid)
                                    .child("Items")
                                    .child(category)
                                    .child(topic)
                                    .setValue(model)
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(v12.getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    })
                                    .addOnSuccessListener((e)->{
                                        Toast.makeText(v12.getContext(), "Item Added", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    });
                        });
                        break;
                    case R.id.item_popup_delete:
//                        position =getAdapterPosition();
//                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                        ref = FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE);
                        break;
                    case R.id.item_popup_share:
                        position =getAdapterPosition();
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "I have "+ItemsCount.get(position)+" "+ItemsArray.get(position)+"/s/oes";
                        sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                        view.getContext().startActivity(Intent.createChooser(sharingIntent,"Share via"));
                        break;
                }
                return false;
            });
        }
    }
}
