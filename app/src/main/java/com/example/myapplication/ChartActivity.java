package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.myapplication.Adapters.ChartFragmentAdapter;
import com.example.myapplication.Adapters.FragmentAdapter;
import com.example.myapplication.databinding.ActivityChartBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {
    BottomNavigationView nav_view;
    ActivityChartBinding binding1;
    ViewPager chart_viewpager;
    TableLayout chart_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding1 = ActivityChartBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_chart);
        setContentView(binding1.getRoot());

        binding1.chartViewpager.setAdapter(new ChartFragmentAdapter(getSupportFragmentManager()));
        binding1.chartTab.setupWithViewPager(binding1.chartViewpager);

        nav_view = findViewById(R.id.nav_view);
        nav_view.setSelectedItemId(R.id.navigation_chartview);
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