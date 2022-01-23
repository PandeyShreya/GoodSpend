package com.example.myapplication;

import static android.app.AlarmManager.INTERVAL_DAY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class ReminderEditActivity extends AppCompatActivity {

    EditText Amount, Note, Date,Time;
    Spinner Category, repeatReminder;
    Button saveButton,deleteButton;
    FirebaseAuth firebaseAuth;
    DatabaseReference reminderReference;
    String timeTonotify;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(String.valueOf(this), "onCreate: called. ");
        setContentView(R.layout.activity_reminder_edit);
        Note = findViewById(R.id.reminder_note_edit);
        Amount = findViewById(R.id.reminder_amount_edit);
        Category = findViewById(R.id.reminder_category_edit);
        Date = findViewById(R.id.reminder_date_edit);
        Time = findViewById(R.id.reminder_time_edit);
        repeatReminder = findViewById(R.id.repeat_reminder_edit);
        saveButton = findViewById(R.id.reminder_edit_button);
        deleteButton = findViewById(R.id.reminder_delete_button);


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

        //inserting data onStart
        Note.setText(getIntent().getStringExtra("note"));
        Amount.setText(getIntent().getStringExtra("amount"));
        Category.setPrompt(getIntent().getStringExtra("category"));
        Date.setText(getIntent().getStringExtra("date"));
        Time.setText(getIntent().getStringExtra("time"));
        repeatReminder.setPrompt(getIntent().getStringExtra("repeatReminder"));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminder();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = Amount.getText().toString().trim();
                String note = Note.getText().toString().trim();
                String reminder_category = Category.getSelectedItem().toString();
                String date = Date.getText().toString().trim();
                String time = Time.getText().toString().trim();
                String repeat_reminder = repeatReminder.getSelectedItem().toString();
                deleteAlarm(note, date, time);

                //deleting data from firebase
                String id = getIntent().getStringExtra("id");
                ReminderData reminderData = new ReminderData(reminder_category, id, note, date, time, repeat_reminder, Double.parseDouble(amount));
                reminderReference.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ReminderEditActivity.this, "Reminder Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ReminderEditActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(getApplicationContext(),AlertsActivity.class));
                        finish();
                    }
                });

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

    private void saveReminder() {
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

        String id = getIntent().getStringExtra("id");
        ReminderData reminderData = new ReminderData(reminder_category, id, note, date, time, repeat_reminder, Double.parseDouble(amount));
        reminderReference.child(id).setValue(reminderData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ReminderEditActivity.this, "Reminder Item updated Successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ReminderEditActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(getApplicationContext(),AlertsActivity.class));
                finish();
            }
        });

        updateAlarm(note, date, time);

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

    private void updateAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

    private void deleteAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pendingIntent);

    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),AlertsActivity.class));
        finish();
    }
}