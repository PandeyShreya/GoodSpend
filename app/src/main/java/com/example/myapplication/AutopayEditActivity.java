package com.example.myapplication;

import static android.app.AlarmManager.INTERVAL_DAY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AutopayEditActivity extends AppCompatActivity {
    EditText Amount, Note, dateLimit,timeLimit;
    Spinner Category, paymentType, RepeatTransaction,ReceiveNotifications;
    Button saveButton,deleteButton;
    FirebaseAuth firebaseAuth;
    DatabaseReference autopayReference,incomeReference, expenseReference;
    Calendar calendar;
    String timeTonotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(String.valueOf(this), "onCreate: called. ");
        setContentView(R.layout.activity_autopay_edit);

        Note = findViewById(R.id.autopay_note_edit);
        Amount = findViewById(R.id.autopay_amount_edit);
        Category = findViewById(R.id.autopay_category_edit);
        paymentType = findViewById(R.id.autopay_payment_edit);
        dateLimit = findViewById(R.id.autopay_date_edit);
        timeLimit = findViewById(R.id.autopay_time_edit);
        RepeatTransaction = findViewById(R.id.autopay_repeat_edit);
        saveButton = findViewById(R.id.autopay_save_button);
        deleteButton = findViewById(R.id.autopay_delete_button);

        String[] autopayCategory = getResources().getStringArray(R.array.category_list);
        String[] paymentMode = getResources().getStringArray(R.array.payment_mode);
        String[] repeatTransaction = getResources().getStringArray(R.array.repeat_list);
        //String[] receiveNotification = getResources().getStringArray(R.array.notify_list);

        firebaseAuth = FirebaseAuth.getInstance();
        autopayReference = FirebaseDatabase.getInstance().getReference().child("autopay").child(firebaseAuth.getCurrentUser().getUid());
        incomeReference = FirebaseDatabase.getInstance().getReference().child("income").child(firebaseAuth.getCurrentUser().getUid());
        expenseReference = FirebaseDatabase.getInstance().getReference().child("expenses").child(firebaseAuth.getCurrentUser().getUid());

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,autopayCategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(adapter);

        ArrayAdapter adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,paymentMode);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentType.setAdapter(adapter1);

        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,repeatTransaction);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RepeatTransaction.setAdapter(adapter2);

        //inserting data onStart
        Note.setText(getIntent().getStringExtra("note"));
        Amount.setText(getIntent().getStringExtra("amount"));
        Category.setPrompt(getIntent().getStringExtra("category"));
        paymentType.setPrompt(getIntent().getStringExtra("payment"));
        dateLimit.setText(getIntent().getStringExtra("dateLimit"));
        timeLimit.setText(getIntent().getStringExtra("timeLimit"));
        RepeatTransaction.setPrompt(getIntent().getStringExtra("repeatTransaction"));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = Amount.getText().toString().trim();
                String note = Note.getText().toString().trim();
                String autopay_category = Category.getSelectedItem().toString();
                String payment = paymentType.getSelectedItem().toString();
                String repeatTransactions = RepeatTransaction.getSelectedItem().toString();
//                String receiveNotifications = ReceiveNotifications.getSelectedItem().toString();
                String date_limit = dateLimit.getText().toString().trim();
                String time = timeLimit.getText().toString().trim();

                String id = getIntent().getStringExtra("id");

                if(TextUtils.isEmpty(amount)){
                    Amount.setError("Amount is a required field");
                    return;
                }

                //saving updated data in firebase at same id
                AutopayData autopayData = new AutopayData(autopay_category, id, note, date_limit,time, payment, repeatTransactions, Double.parseDouble(amount));
                autopayReference.child(id).setValue(autopayData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AutopayEditActivity.this, "Autopay Item updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AutopayEditActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if(autopay_category.equals("Savings(Income)") || autopay_category.equals("Salary(Income)") || autopay_category.equals("Deposit(Income)") || autopay_category.equals("Others(Income)")){
                    getAutopayIncomeAmount(date_limit, id, note, autopay_category, amount);
                }
                else{
                    getAutopayExpenseAmount(date_limit, id, note, autopay_category, amount);
                }
                updateAlarm(note, date_limit, time, autopay_category, amount);



            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = Amount.getText().toString().trim();
                String note = Note.getText().toString().trim();
                String autopay_category = Category.getSelectedItem().toString();
                String payment = paymentType.getSelectedItem().toString();
                String repeatTransactions = RepeatTransaction.getSelectedItem().toString();
//                String receiveNotifications = ReceiveNotifications.getSelectedItem().toString();
                String date_limit = dateLimit.getText().toString().trim();
                String time = timeLimit.getText().toString().trim();

                deleteAlarm(note, date_limit, time, autopay_category, amount);

                //deleting data from firebase
                String id = getIntent().getStringExtra("id");
                AutopayData autopayData = new AutopayData(autopay_category, id, note, date_limit, time,payment, repeatTransactions, Double.parseDouble(amount));
                autopayReference.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AutopayEditActivity.this, "Autopay Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AutopayEditActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                        //startActivity(new Intent(getApplicationContext(),AlertsActivity.class));
                        finish();
                    }
                 });
              }
        });
        dateLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autopay_date();
            }
        });
        timeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autopay_time();
            }
        });
    }

    public void getAutopayIncomeAmount(String date_limit, String id, String note, String autopay_category, String amount) {

        IncomeData incomeData = new IncomeData(date_limit,id, note, autopay_category,Integer.parseInt(amount));
        incomeReference.child(id).setValue(incomeData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AutopayEditActivity.this, "Autopay Income Item updated Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AutopayHome.class));
                    finish();
                }
                else{
                    Toast.makeText(AutopayEditActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void getAutopayExpenseAmount(String date_limit, String id, String note, String autopay_category, String amount){
        ExpenseData expenseData = new ExpenseData(date_limit,id, note, autopay_category,Integer.parseInt(amount));
        expenseReference.child(id).setValue(expenseData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AutopayEditActivity.this, "Autopay Expense Item updated Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AutopayHome.class));
                    finish();
                }
                else{
                    Toast.makeText(AutopayEditActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void autopay_time() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;
                timeLimit.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }

    private void autopay_date() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateLimit.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    
    private void updateAlarm(String text, String date, String time, String category, String amount) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("category", category);
        intent.putExtra("amount", amount);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            if(RepeatTransaction.equals("Daily")){
                am.setRepeating( AlarmManager.RTC_WAKEUP,date1.getTime(), INTERVAL_DAY,pendingIntent);
            }
            else if(RepeatTransaction.equals("Weekly")) {
                am.setRepeating( AlarmManager.RTC_WAKEUP,date1.getTime(),AlarmManager.INTERVAL_DAY*calendar.getActualMaximum(Calendar.DAY_OF_WEEK),pendingIntent);
            }
            else if(RepeatTransaction.equals("Monthly")) {
                am.setRepeating( AlarmManager.RTC_WAKEUP,date1.getTime(),AlarmManager.INTERVAL_DAY*calendar.getActualMaximum(Calendar.DAY_OF_MONTH),pendingIntent);

            }
            else if(RepeatTransaction.equals("Yearly")) {
                am.setRepeating( AlarmManager.RTC_WAKEUP,date1.getTime(), AlarmManager.INTERVAL_DAY*calendar.getActualMaximum(Calendar.DAY_OF_YEAR),pendingIntent);

            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        finish();

    }

    private void deleteAlarm(String text, String date, String time, String category, String amount) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("category", category);
        intent.putExtra("amount", amount);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pendingIntent);

    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),AutopayHome.class));
        finish();
    }
}