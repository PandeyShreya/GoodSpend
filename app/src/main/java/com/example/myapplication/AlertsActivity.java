package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Adapters.ReminderAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlertsActivity extends AppCompatActivity implements ReminderAdapter.OnItemClickListener {
    BottomNavigationView nav_view;
    Button reminder_add_button, reminder_edit_button;
    FloatingActionButton reminder_add;
    RecyclerView reminderRecyclerView;
    ReminderAdapter reminderAdapter;
    ArrayList<ReminderData> arrayList;
    DatabaseReference reminderReference;
    FirebaseAuth firebaseAuth;
    private String onlineUserID;
    private ReminderAdapter.OnItemClickListener reminderList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

//        reminder_add_button = findViewById(R.id.reminder_add_button);
//        reminder_edit_button = findViewById(R.id.reminder_edit_button);
        reminder_add = findViewById(R.id.reminder_add);
        reminderRecyclerView = findViewById(R.id.reminder_recycle);

        firebaseAuth = FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reminderReference = FirebaseDatabase.getInstance().getReference("reminders").child(onlineUserID);

        reminderRecyclerView.setHasFixedSize(true);
        reminderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //reminderAdapter.setOnClickListner();
        arrayList = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(this, arrayList,this);
        reminderRecyclerView.setAdapter(reminderAdapter);
//        reminderRecyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reminderEdit();
//            }
//        });


        nav_view = findViewById(R.id.nav_view);
        nav_view.setSelectedItemId(R.id.navigation_alerts);
        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_transactions:
                        startActivity(new Intent(getApplicationContext(),TransactionActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_chartview:
                        startActivity(new Intent(getApplicationContext(),ChartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_alerts:
                        return true;
                    case R.id.navigation_autoadd:
                        startActivity(new Intent(getApplicationContext(),AutopayHome.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return  false;
            }
        });

        reminder_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddReminderActivity.class));
            }
        });
        getRecyclerView();


    }

//    private void reminderEdit() {
//        reminderList = new ReminderAdapter.OnItemClickListener(){
//
//            @Override
//            public void onClick(View view, int position) {
//
//            }
//        };
//    }

    private void getRecyclerView() {
        reminderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReminderData reminderData = dataSnapshot.getValue(ReminderData.class);
                    arrayList.add(reminderData);
                }
                reminderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view, int position) {
        Log.d(String.valueOf(this),"ReminderList Clicked. "+position);
        //another way to pass intent
//        Intent intent = new Intent(getApplicationContext(), ReminderEditActivity.class);
//        // startActivity(new Intent(getApplicationContext(),ReminderEditActivity.class));
//        // intent.putExtra("note",arrayList.get(position).getNote());
//        intent.putExtra("note","shreya");
//        startActivity(intent);
    }
}