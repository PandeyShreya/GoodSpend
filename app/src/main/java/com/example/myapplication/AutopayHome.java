package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Adapters.AutopayAdapter;
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

public class AutopayHome extends AppCompatActivity implements AutopayAdapter.OnItemClickListener {
    BottomNavigationView nav_view;
    Button autopay_add_button,autopay_edit_button;
    FloatingActionButton autopay_add;
    RecyclerView autopayRecyclerView;
    AutopayAdapter autopayAdapter;
    ArrayList<AutopayData> arrayList;
    DatabaseReference autopayReference;
    FirebaseAuth firebaseAuth;
    private String onlineUserID;
    private AutopayAdapter.OnItemClickListener autopayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autopay_home);

//        autopay_add_button = findViewById(R.id.autopay_add_button);
        autopay_add = findViewById(R.id.autopay_add);
//        autopay_edit_button = findViewById(R.id.autopay_edit_button);
        autopayRecyclerView = findViewById(R.id.autopay_recycle);

        firebaseAuth = FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        autopayReference = FirebaseDatabase.getInstance().getReference("autopay").child(onlineUserID);

        autopayRecyclerView.setHasFixedSize(true);
        autopayRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList = new ArrayList<>();
        autopayAdapter = new AutopayAdapter(this, arrayList,autopayList);
        autopayRecyclerView.setAdapter(autopayAdapter);

        nav_view = findViewById(R.id.nav_view);
        nav_view.setSelectedItemId(R.id.navigation_autoadd);
        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_transactions:
                        startActivity(new Intent(getApplicationContext(),TransactionActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_chartview:
                        startActivity(new Intent(getApplicationContext(),ChartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_alerts:
                        startActivity(new Intent(getApplicationContext(),AlertsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_autoadd:
                        return true;

                }
                return  false;
            }
        });
        autopay_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AutopayActivity.class));
                finish();
            }
        });
        getRecyclerView();
//        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
//        manager.cancelAll();
//
//        AutopayActivity autopayActivity = new AutopayActivity();
//
//        if(getIntent().hasExtra("yes")){
//            if(autopayActivity.autopay_category.equals("Savings(Income)") || autopayActivity.autopay_category.equals("Salary(Income)") || autopayActivity.autopay_category.equals("Deposit(Income)") || autopayActivity.autopay_category.equals("Others(Income)")){
//                autopayActivity.getAutopayIncomeAmount(autopayActivity.date_limit, autopayActivity.id, autopayActivity.note, autopayActivity.autopay_category, autopayActivity.amount);
//            }
//            else{
//                autopayActivity.getAutopayExpenseAmount(autopayActivity.date_limit, autopayActivity.id, autopayActivity.note, autopayActivity.autopay_category, autopayActivity.amount);
//            }
//        }
//        else if(getIntent().hasExtra("no")){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//        }
    }

    private void getRecyclerView() {
        autopayReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AutopayData autopayData = dataSnapshot.getValue(AutopayData.class);
                    arrayList.add(autopayData);
                }
                autopayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //if autopay list is clicked for edit
    @Override
    public void onClick(View view, int position) {
        Log.d(String.valueOf(this),"AutopayList Clicked. "+position);
    }
}