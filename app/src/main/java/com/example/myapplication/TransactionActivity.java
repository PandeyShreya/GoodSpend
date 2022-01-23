package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.example.myapplication.Adapters.FragmentAdapter;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.ActivityTransactionBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TransactionActivity extends AppCompatActivity {
    ActivityTransactionBinding binding;
    ViewPager transViewPager;
    TableLayout transactionTab;
    BottomNavigationView nav_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_transaction);
        setContentView(binding.getRoot());

        binding.transViewpager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.transactionTab.setupWithViewPager(binding.transViewpager);

        nav_view = findViewById(R.id.nav_view);
        nav_view.setSelectedItemId(R.id.navigation_transactions);
        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_transactions:
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
                        startActivity(new Intent(getApplicationContext(),AlertsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_autoadd:
                        startActivity(new Intent(getApplicationContext(),AutopayHome.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return  false;
            }
        });



    }
}