package com.track.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.track.trackandtrigger.Modal.ItemsModel;

public class ItemActivity extends AppCompatActivity {
    TextView ItemNameText,ItemCountText,textItemName;
    ImageView add_new_camera,itemImg;
    StorageReference storageReference;
    Uri itemImgUri;
    String topic,count,category,imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
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
        if(!imageUrl.equals("")){
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic__category)
                    .into(itemImg);
        }
        ItemNameText.setText(topic);
        ItemCountText.setText(count);
        textItemName.setText(topic);
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