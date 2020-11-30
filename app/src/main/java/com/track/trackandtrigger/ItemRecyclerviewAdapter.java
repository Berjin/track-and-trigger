package com.track.trackandtrigger;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.track.trackandtrigger.Modal.ItemsModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class ItemRecyclerviewAdapter extends FirebaseRecyclerAdapter<ItemsModel, ItemRecyclerviewAdapter.ViewHolder> {

    private ArrayList<String> ItemsArray = new ArrayList<>();
    private ArrayList<String> ItemsCount = new ArrayList<>();
    private String[] ItemsCategory = new String[100];
    private String[] ImageUrls = new String[100];
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
        ImageUrls[position]=model.getImageUrl();
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
                    intent.putExtra("category",ItemsCategory[position]);
                    intent.putExtra("image_url",ImageUrls[position]);
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
                        position =getAdapterPosition();
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ref = FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE);
                        ref.child(uid).child("Items").child(ItemsCategory[position]).child(ItemsArray.get(position)).removeValue();
                        Toast.makeText(view.getContext(), "deleted", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_popup_share:
                        // android 7.0 system to solve the problem of taking pictures
                        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder1.build());
                        builder1.detectFileUriExposure();
                        shareItem(ImageUrls[position]);
                        break;
                }
                return false;
            });
        }
        public Uri getLocalBitmapUri(Bitmap bmp) {
            Uri bmpUri = null;
            try {
                File file =  new File(itemView.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), ItemsArray.get(position) + System.currentTimeMillis() + ".png");
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                bmpUri = Uri.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmpUri;
        }
        public void shareItem(String url) {
            if(!url.equals("")) {
                Picasso.get().load(url).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        String shareBody = "I have " + ItemsCount.get(position) + " " + ItemsArray.get(position) + "/s/oes";
                        i.setType("image/*");
                        i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                        i.putExtra(Intent.EXTRA_TEXT, shareBody);
                        itemView.getContext().startActivity(Intent.createChooser(i, "Share Via"));

                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
            }else{
                Intent i = new Intent(Intent.ACTION_SEND);
                String shareBody = "I have " + ItemsCount.get(position) + " " + ItemsArray.get(position) + "/s/oes";
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, shareBody);
                itemView.getContext().startActivity(Intent.createChooser(i, "Share Via"));

            }
        }
    }
}
