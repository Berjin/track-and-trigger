package com.track.trackandtrigger;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.track.trackandtrigger.Modal.ItemsModel;
import com.track.trackandtrigger.Modal.RemindersModel;
import com.track.trackandtrigger.Modal.UserInfoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import java.util.Random;


import static java.lang.Integer.parseInt;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    ImageView close_bottom_sheet;
    LinearLayout add_item,add_reminder;

    private FirebaseDatabase database;
    private DatabaseReference userInfoRef;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    long alarmStartTime;
    int rno=0;
    private ArrayList<String> categoryTitles = new ArrayList<>();
    private Set<String> categoryTitlesSet = new HashSet<>();
    ValueEventListener listener;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.fab);
        bottomSheet = findViewById(R.id.bottom_sheet);
        close_bottom_sheet = findViewById(R.id.close_bottom_sheet);
        add_item = findViewById(R.id.add_item);
        add_reminder = findViewById(R.id.add_reminder);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        close_bottom_sheet.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        fab.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        getCategoryTitles();
        add_item.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AddItemDialogTheme);
            View itemView = LayoutInflater.from(this).inflate(R.layout.add_item,null);
            final int[] count = {0};
            TextInputEditText item_topic = itemView.findViewById(R.id.item_topic);
            TextInputLayout item_topic_layout = itemView.findViewById(R.id.item_topic_layout);
            TextInputEditText item_count = itemView.findViewById(R.id.item_count);
            TextInputLayout item_count_layout = itemView.findViewById(R.id.item_count_layout);
            AutoCompleteTextView add_category = itemView.findViewById(R.id.add_category);
            TextInputLayout add_category_layout = itemView.findViewById(R.id.add_category_layout);
            ImageView increase_count= itemView.findViewById(R.id.increase_count);
            ImageView decrease_count= itemView.findViewById(R.id.decrease_count);
            //View
            builder.setView(itemView);
            AlertDialog dialog = builder.create();
            dialog.show();
            increase_count.setOnClickListener(v1 -> {
                count[0] = parseInt(item_count.getText().toString().trim());
                count[0]++;
                item_count.setText(""+count[0]);
            });
            decrease_count.setOnClickListener(v1 -> {
                count[0] = parseInt(item_count.getText().toString().trim());
                if(count[0]>0){
                    count[0]--;
                }
                item_count.setText(""+count[0]);
            });

            String[] items = new String[]{
                    "Groceries",
                    "Meetings"
            };
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    R.layout.dropdown_profession,
                    getCategoryTitles()
            );
            add_category.setAdapter(adapter);
            Button btn_add_item = itemView.findViewById(R.id.btn_add_item);
            btn_add_item.setOnClickListener(v12 -> {
                if(TextUtils.isDigitsOnly(add_category.getText().toString().trim())){
                    add_category_layout.setHelperText("*Enter the category name");
                    add_category_layout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    add_category_layout.setHelperTextEnabled(true);
                }
                else if(TextUtils.isEmpty(item_topic.getText().toString().trim())){
                    item_topic_layout.setHelperText("*Enter the item topic");
                    item_topic_layout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    item_topic_layout.setHelperTextEnabled(true);
                    add_category_layout.setHelperTextEnabled(false);
                }
                else if(TextUtils.isEmpty(item_count.getText().toString().trim())){
                    item_count_layout.setHelperText("*Enter the number of items");
                    item_count_layout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    item_count_layout.setHelperTextEnabled(true);
                    item_topic_layout.setHelperTextEnabled(false);
                    add_category_layout.setHelperTextEnabled(false);
                }
                else{
                    ItemsModel model = new ItemsModel();
                    database = FirebaseDatabase.getInstance();
                    userInfoRef = database.getReference(Common.USER_INFO_REFERENCE);
                    String category = add_category.getText().toString().trim();
                    String topic = item_topic.getText().toString().trim();
                    model.topic = item_topic.getText().toString().trim();
                    model.itemCount = item_count.getText().toString().trim();
                    model.category = category;
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    userInfoRef.child(uid)
                            .child("Items")
                            .child(category)
                            .child(topic)
                            .setValue(model)
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            })
                            .addOnSuccessListener((e)->{
                                Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            });
                }
            });
        });
        add_reminder.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AddItemDialogTheme);
            View itemView = LayoutInflater.from(this).inflate(R.layout.add_reminder,null);
            TextInputEditText reminder_title = itemView.findViewById(R.id.reminder_title);
            TextInputLayout reminder_title_layout = itemView.findViewById(R.id.reminder_title_layout);
            TextInputEditText reminder_datetime = itemView.findViewById(R.id.reminder_datetime);
            TextInputLayout reminder_datetime_layout = itemView.findViewById(R.id.reminder_datetime_layout);
            reminder_datetime.setInputType(InputType.TYPE_NULL);
            //View
            builder.setView(itemView);
            AlertDialog dialog = builder.create();
            dialog.show();
            Button btn_add_reminder = itemView.findViewById(R.id.btn_add_reminder);
            reminder_datetime.setOnClickListener(v1 -> {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                    TimePickerDialog.OnTimeSetListener timeSetListener = (view1, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                        reminder_datetime.setText(simpleDateFormat.format(calendar.getTime()));

                        // Intent
                         rno=new Random().nextInt(900000) + 100000;
                        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                          intent.putExtra("notificationId", rno);
                          String userDisplayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                         intent.putExtra("message","Dear "+userDisplayName+",\n\n"+"You have a reminder scheduled now.\n"+"Title: "+reminder_title.getText().toString().trim());

                        // PendingIntent
                         pendingIntent = PendingIntent.getBroadcast(
                                MainActivity.this,rno, intent, PendingIntent.FLAG_CANCEL_CURRENT
                        );

                        // AlarmManager
                         alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        startTime.set(Calendar.MINUTE, minute);
                        startTime.set(Calendar.SECOND, hourOfDay);
                        startTime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        startTime.set(Calendar.YEAR,year);


                         alarmStartTime = startTime.getTimeInMillis();

                        // Set Alarm

                    };
                    new TimePickerDialog(MainActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
                };
                new DatePickerDialog(MainActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            });

            btn_add_reminder.setOnClickListener(v12 -> {
                if(TextUtils.isDigitsOnly(reminder_title.getText().toString().trim())){
                    reminder_title_layout.setHelperText("*Enter reminder title");
                    reminder_title_layout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    reminder_title_layout.setHelperTextEnabled(true);
                }
                else{
                    final int[] LatestReminderId = new int[1];
                    RemindersModel model = new RemindersModel();
                    database = FirebaseDatabase.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    userInfoRef = database.getReference(Common.USER_INFO_REFERENCE);
                    String title = reminder_title.getText().toString().trim();
                    model.title = reminder_title.getText().toString().trim();
                    model.datetime = reminder_datetime.getText().toString().trim();
                    model.isDone = false;
                    model.reminder_id=rno;
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    userInfoRef.child(uid)
                            .child("Reminders")
                            .child(title)
                            .setValue(model)
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            })
                            .addOnSuccessListener((e)->{
                                Toast.makeText(this, "Reminder Added", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
                                Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
                            });
                }
            });
        });

    }

//    private void setLocale(String lang) {
//            Locale myLocale = new Locale(lang);
//            Resources resources = getResources();
//            DisplayMetrics dm = resources.getDisplayMetrics();
//            Configuration configuration = resources.getConfiguration();
//            configuration.locale = myLocale;
//            resources.updateConfiguration(configuration, dm);
//            recreate();
//    }

    private ArrayList<String> getCategoryTitles() {
        ref = FirebaseDatabase.getInstance().getReference(Common.USER_INFO_REFERENCE);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        snapshot.child("Items").getChildren().iterator().forEachRemaining(v -> categoryTitlesSet.add(v.getKey()));
                        for (String i : categoryTitlesSet) {
                            if (!categoryTitles.contains(i)) {
                                categoryTitles.add(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(listener);
        return categoryTitles;
    }

    Home homeFragment = new Home();
    Diary diaryFragment = new Diary();
    Reminders remindersFragment = new Reminders();
    Profile profileFragment = new Profile();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container,homeFragment).commitNow();
                return true;
            case R.id.diary:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container,diaryFragment).commitNow();
                return true;
            case R.id.reminder:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container,remindersFragment).commitNow();
                return true;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container,profileFragment).commitNow();
                return true;
        }
        return false;
    }
}
