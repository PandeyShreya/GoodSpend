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

import com.example.myapplication.databinding.ActivityAlertsBinding;
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

public class AddReminderActivity extends AppCompatActivity {
    EditText Amount, Note, Date,Time;
    Spinner Category, repeatReminder;
    Button SetButton;
    FirebaseAuth firebaseAuth;
    DatabaseReference reminderReference;
    String timeTonotify;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        Amount = findViewById(R.id.reminder_amount);
        Note = findViewById(R.id.reminder_note);
        Date = findViewById(R.id.reminder_date);
        Time = findViewById(R.id.reminder_time);
        Category = findViewById(R.id.reminder_category);
        repeatReminder = findViewById(R.id.repeat_reminder);
        SetButton = findViewById(R.id.set_button);

        createNotificationChannel();

        String[] expenseList = getResources().getStringArray(R.array.expense_list);
        String[] repeatReminderList = getResources().getStringArray(R.array.repeat_reminder);

        firebaseAuth = FirebaseAuth.getInstance();
        reminderReference = FirebaseDatabase.getInstance().getReference().child("reminders").child(firebaseAuth.getCurrentUser().getUid());

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,expenseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(adapter);

        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,repeatReminderList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatReminder.setAdapter(adapter2);

        SetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = Amount.getText().toString().trim();
                String note = Note.getText().toString().trim();
                String reminder_category = Category.getSelectedItem().toString();
                String date = Date.getText().toString().trim();
                String time = Time.getText().toString().trim();
                String repeat_reminder = repeatReminder.getSelectedItem().toString();

                if(TextUtils.isEmpty(amount)){
                    Amount.setError("Amount is a required field");
                    return;
                }
                String id = reminderReference.push().getKey();
                ReminderData reminderData = new ReminderData(reminder_category, id, note, date, time, repeat_reminder, Double.parseDouble(amount));
                reminderReference.child(id).setValue(reminderData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddReminderActivity.this, "Reminder Item added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AddReminderActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(getApplicationContext(),AlertsActivity.class));
                        finish();

                    }
                });
                
                setAlarm(note, date, time);

            }

        });
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminder_date();
            }
        });
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminder_time();
            }
        });
    }



    private void reminder_time() {
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


    private void reminder_date() {
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
            CharSequence name = "FinanceManagerChannel";
            String description = "Channel For Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("FinanceManagerChannel", name, importance);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            if(repeatReminder.equals("Daily")){
                am.setRepeating( AlarmManager.RTC_WAKEUP,date1.getTime(), INTERVAL_DAY,pendingIntent);
            }
            else if(repeatReminder.equals("Weekly")) {
                am.setRepeating( AlarmManager.RTC_WAKEUP,date1.getTime(),AlarmManager.INTERVAL_DAY*calendar.getActualMaximum(Calendar.DAY_OF_WEEK),pendingIntent);
            }
            else if(repeatReminder.equals("Monthly")) {
                am.setRepeating( AlarmManager.RTC_WAKEUP,date1.getTime(),AlarmManager.INTERVAL_DAY*calendar.getActualMaximum(Calendar.DAY_OF_MONTH),pendingIntent);
            }
            else if(repeatReminder.equals("Yearly")) {
                am.setRepeating( AlarmManager.RTC_WAKEUP,date1.getTime(), AlarmManager.INTERVAL_DAY*calendar.getActualMaximum(Calendar.DAY_OF_YEAR),pendingIntent);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        finish();

    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), AlertsActivity.class));
        finish();
    }


}