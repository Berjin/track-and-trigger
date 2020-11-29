package com.track.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.track.trackandtrigger.Modal.DiaryModel;

import javax.mail.UIDFolder;

public class AddDiaryActivity extends AppCompatActivity {
    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextInputEditText textInputEditTextTitle;
    private TextInputEditText textInputEditTextDescription;
    //date time variable

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_diary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_diary_menu:
                saveDiary();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private void saveDiary() {
        String title = textInputEditTextTitle.getText().toString();
        String description = textInputEditTextDescription.getText().toString();
        //date variable needed

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        String datetime = "29/11/2020";
        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection("Notebook");
        notebookRef.add(new DiaryModel(title, description, datetime));
        Toast.makeText(this, "Diary entry added", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textInputEditTextTitle = findViewById(R.id.edit_text_title);
        textInputEditTextDescription = findViewById(R.id.edit_text_description);
    }

}