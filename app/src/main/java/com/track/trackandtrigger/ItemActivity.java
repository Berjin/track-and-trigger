package com.track.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.track.trackandtrigger.Modal.ItemsModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ItemActivity extends AppCompatActivity {
    TextView ItemNameText,ItemCountText,textItemName;
    ImageView add_new_camera,itemImg,item_share;
    StorageReference storageReference;
    Uri itemImgUri;
    String topic,count,category,imageUrl;
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), topic + System.currentTimeMillis() + ".png");
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
                    String shareBody = "I have " + count + " " + topic + "/s/oes";
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                    i.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(i, "Share Via"));

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
            String shareBody = "I have " + count + " " + topic + "/s/oes";
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(i, "Share Via"));

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        // android 7.0 system to solve the problem of taking pictures
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        Intent intent =getIntent();
        topic = intent.getStringExtra("Topic");
        count = intent.getStringExtra("count");
        category = intent.getStringExtra("category");
        imageUrl = intent.getStringExtra("image_url");
        storageReference = FirebaseStorage.getInstance().getReference();
        ItemNameText = findViewById(R.id.ItemNameText);
        ItemCountText = findViewById(R.id.ItemCountText);
        add_new_camera = findViewById(R.id.add_new_camera);
        textItemName =findViewById(R.id.textItemName);
        itemImg = findViewById(R.id.itemImg);
        item_share = findViewById(R.id.item_share);
        if(!imageUrl.equals("")){
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic__category)
                    .into(itemImg);
        }
        ItemNameText.setText(topic);
        ItemCountText.setText(count);
        textItemName.setText(topic);
        item_share.setOnClickListener(v->{
            shareItem(imageUrl);
        });

        add_new_camera.setOnClickListener(v->{
            Intent intent1 = new Intent();
            intent1.setType("image/'");
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent1,1);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            itemImgUri = data.getData();
            Glide.with(this)
                    .load(itemImgUri)
                    .placeholder(R.drawable.ic__category)
                    .into(itemImg);
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
            uploadImage();
        }
    }
    private String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference ref = storageReference.child("ItemImages").child(uid).child(category).child(topic+"."+getExtension(itemImgUri));
        ref.putFile(itemImgUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(ItemActivity.this, "Upload success", Toast.LENGTH_SHORT).show();
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener((OnSuccessListener<Uri>) uri -> {
                        imageUrl = uri.toString();
                        ItemsModel model = new ItemsModel();
                        model.topic = topic;
                        model.category =category;
                        model.itemCount =count;
                        model.imageUrl = imageUrl;
                        FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE).child(uid).child("Items").child(category).child(topic).setValue(model);

                    }).addOnFailureListener((OnFailureListener) e -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(ItemActivity.this, "upload failed", Toast.LENGTH_SHORT).show());
    }
}