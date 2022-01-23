package com.example.myapplication;

import static android.app.AlarmManager.INTERVAL_DAY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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

public class AutopayActivity extends AppCompatActivity {
    EditText Amount, Note, Date,Time;
    Spinner Category, PaymentType, RepeatTransaction,ReceiveNotifications;
    Button DoneButton;
    FirebaseAuth firebaseAuth;
    DatabaseReference autopayReference,incomeReference, expenseReference;
    Calendar calendar;
    String timeTonotify;
//    int start=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autopay);

        Amount = findViewById(R.id.autopay_amount);
        Note = findViewById(R.id.auto_note);
        Date = findViewById(R.id.date_limit);
        Time = findViewById(R.id.autopay_time);
        Category = findViewById(R.id.auto_category);
        PaymentType = findViewById(R.id.autopay_type);
        RepeatTransaction = findViewById(R.id.repeat_transaction);
//        ReceiveNotifications = findViewById(R.id.receive_notification);
        DoneButton = findViewById(R.id.done_button);

        createNotificationChannel();

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
        PaymentType.setAdapter(adapter1);

        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,repeatTransaction);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RepeatTransaction.setAdapter(adapter2);

//        ArrayAdapter adapter3 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,receiveNotification);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////        ReceiveNotifications.setAdapter(adapter3);
//        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
//        manager.cancelAll();
//
//        if(getIntent().hasExtra("yes")){
//            if(autopay_category.equals("Savings(Income)") || autopay_category.equals("Salary(Income)") || autopay_category.equals("Deposit(Income)") || autopay_category.equals("Others(Income)")){
//                getAutopayIncomeAmount(date_limit, id, note, autopay_category, amount);
//            }
//            else{
//                getAutopayExpenseAmount(date_limit, id, note, autopay_category, amount);
//            }
//        }
//        else if(getIntent().hasExtra("no")){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//        }


        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = Amount.getText().toString().trim();
                String note = Note.getText().toString().trim();
                String autopay_category = Category.getSelectedItem().toString();
                String paymentType = PaymentType.getSelectedItem().toString();
                String repeatTransactions = RepeatTransaction.getSelectedItem().toString();
//                String receiveNotifications = ReceiveNotifications.getSelectedItem().toString();
                String date_limit = Date.getText().toString().trim();
                String time = Time.getText().toString().trim();

                String id = autopayReference.push().getKey();

                if(TextUtils.isEmpty(amount)){
                    Amount.setError("Amount is a required field");
                    return;
                }

                AutopayData autopayData = new AutopayData(autopay_category, id, note, date_limit, time, paymentType, repeatTransactions, Double.parseDouble(amount));
                autopayReference.child(id).setValue(autopayData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AutopayActivity.this, "Autopay Item added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AutopayActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if(autopay_category.equals("Savings(Income)") || autopay_category.equals("Salary(Income)") || autopay_category.equals("Deposit(Income)") || autopay_category.equals("Others(Income)")){
                    getAutopayIncomeAmount(date_limit, id, note, autopay_category, amount);
                }
                else{
                    getAutopayExpenseAmount(date_limit, id, note, autopay_category, amount);
                }
                setAlarm(note, date_limit, time, autopay_category, amount);



            }
        });

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autopay_date();
            }
        });
        Time.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(AutopayActivity.this, "Autopay Income Item added Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AutopayHome.class));
                    finish();
                }
                else{
                    Toast.makeText(AutopayActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AutopayActivity.this, "Autopay Expense Item added Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AutopayHome.class));
                    finish();
                }
                else{
                    Toast.makeText(AutopayActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
                Time.setText(FormatTime(i, i1));
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
                Date.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "FinanceManagerChannel1";
            String description = "Channel For Autopay";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("FinanceManagerChannel1", name, importance);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void setAlarm(String text, String date, String time, String category, String amount) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("category", category);
        intent.putExtra("amount", amount);

//        if(start==0) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String dateandtime = date + " " + timeTonotify;
            DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
//            start=1;
            try {
                Date date1 = formatter.parse(dateandtime);
                am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
                if (RepeatTransaction.equals("Daily")) {
                    am.setRepeating(AlarmManager.RTC_WAKEUP, date1.getTime(), INTERVAL_DAY, pendingIntent);
                } else if (RepeatTransaction.equals("Weekly")) {
                    am.setRepeating(AlarmManager.RTC_WAKEUP, date1.getTime(), AlarmManager.INTERVAL_DAY * calendar.getActualMaximum(Calendar.DAY_OF_WEEK), pendingIntent);
                } else if (RepeatTransaction.equals("Monthly")) {
                    am.setRepeating(AlarmManager.RTC_WAKEUP, date1.getTime(), AlarmManager.INTERVAL_DAY * calendar.getActualMaximum(Calendar.DAY_OF_MONTH), pendingIntent);

                } else if (RepeatTransaction.equals("Yearly")) {
                    am.setRepeating(AlarmManager.RTC_WAKEUP, date1.getTime(), AlarmManager.INTERVAL_DAY * calendar.getActualMaximum(Calendar.DAY_OF_YEAR), pendingIntent);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            finish();
//        }
//        else if(start==1)
//        {
//            start=0;
//        }

    }
    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), AutopayHome.class));
        finish();
    }

}