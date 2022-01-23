package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    TextView income_display, expenses_display,balance_display,Warning;
    Button income_button, expenses_button;
    BottomNavigationView nav_view;
    Toolbar toolbar;
    private String onlineUserID;
    private DatabaseReference incomeRef, expensesRef, balanceRef;

    private double totalAmountIncome = 0;
    private double totalAmountExpenses = 0;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        income_display = findViewById(R.id.income_display);
        expenses_display = findViewById(R.id.expenses_display);
        balance_display = findViewById(R.id.balance_display);
        income_button = findViewById(R.id.income_button);
        expenses_button = findViewById(R.id.expenses_button);
        toolbar = findViewById(R.id.toolbar);

        Warning=(TextView) findViewById(R.id.WarningMessage);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        incomeRef = FirebaseDatabase.getInstance().getReference("income").child(onlineUserID);
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        balanceRef = FirebaseDatabase.getInstance().getReference("balance").child(onlineUserID);
        setSupportActionBar(toolbar);


        nav_view = findViewById(R.id.nav_view);
        nav_view.setSelectedItemId(R.id.navigation_home);

        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
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
                        startActivity(new Intent(getApplicationContext(),AutopayHome.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return  false;
            }
        });

        getIncomeAmount();
        getExpenseAmount();
        getBalanceAmount();

    }

    private void getIncomeAmount() {
        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object totalIncome = map.get("amount");
                        double pTotalIncome = Double.parseDouble(String.valueOf(totalIncome));
                        totalAmountIncome += pTotalIncome;
                        income_display.setText((String.valueOf(totalAmountIncome)));
                    }
                }
                else{
                    totalAmountIncome=0.00;
                    income_display.setText(String.valueOf(totalAmountIncome));
                }
                balanceRef.child("income").setValue(totalAmountIncome);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getExpenseAmount() {
        expensesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object totalExpense = map.get("amount");
                        double pTotalExpense = Double.parseDouble(String.valueOf(totalExpense));
                        totalAmountExpenses += pTotalExpense;
                        expenses_display.setText((String.valueOf(totalAmountExpenses)));
                    }

                }
                else{
                    totalAmountExpenses=0.00;
                    expenses_display.setText(String.valueOf(totalAmountExpenses));
                }
                balanceRef.child("expenses").setValue(totalAmountExpenses);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getBalanceAmount() {
        balanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    double income;
                    if(snapshot.hasChild("income")){
                        income = Double.parseDouble(snapshot.child("income").getValue().toString());
                    }
                    else{
                        income = 0;
                    }
                    double expense;
                    if(snapshot.hasChild("expenses")){
                        expense = Double.parseDouble(snapshot.child("expenses").getValue().toString());
                    }
                    else{
                        expense = 0;
                    }

                    double balance = income - expense;
                    balance_display.setText(String.valueOf(balance));

                    double balancePercent = balance/income * 100;
                    if(balancePercent >= 20.00){
                        String text = "Congrats! You saved "+new DecimalFormat("##.##").format(balancePercent) +"% of your Income";
                        Warning.setTextColor(Color.parseColor("#4CAF50"));
                        Warning.setText(text);
                    }
                    else if(balancePercent<0.0){
                        Warning.setText("Warning! Expense is Exceeding Income!");
                    }
                    else{
                        Warning.setText("Warning! Savings are only "+new DecimalFormat("##.##").format(balancePercent)+"%");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
//            case R.id.add_member:
//                //Toast.makeText(this , "Add Member", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), AddMember.class));         //open add member page
//                break;
            case R.id.app_setting:
              Toast.makeText(this , "Edit Profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));           //open profile page

                break;
            case R.id.user_profile:
                Toast.makeText(this , "User Profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Profile.class));           //open profile page
                //finish();
                break;
            case R.id.app_logout:
                FirebaseAuth.getInstance().signOut(); //logs out current user
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    public void income(View view) {
        startActivity(new Intent(getApplicationContext(), AddIncomeActivity.class));
        finish();
    }
    public void expenses(View view) {
        startActivity(new Intent(getApplicationContext(), AddExpenseActivity.class));
        finish();
    }

}