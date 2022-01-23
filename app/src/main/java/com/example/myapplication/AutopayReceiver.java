package com.example.myapplication;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AutopayReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String text = bundle.getString("event");
        String date = bundle.getString("date") + " " + bundle.getString("time");
        String category = bundle.getString("category");
        String amount = bundle.getString("amount");

        //When you click on notification
        Intent intent1 = new Intent(context, AutopayReceiver.class);
        intent1.putExtra("message", text);
        intent1.putExtra("yes", true);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0,intent1,PendingIntent.FLAG_ONE_SHOT);

        Intent intent2 = new Intent(context, AutopayReceiver.class);
        intent2.putExtra("message", text);
        intent2.putExtra("no", false);
        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0,intent2,PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "FinanceManagerChannel1");
//                .setSmallIcon(R.drawable.ic_alarm_white_24dp)
//                .setContentTitle("Alarm Manager")
//                .setContentText("Reminder")
//                .setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent);
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.autopay_notification);
        // contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
//        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);
        contentView.setTextViewText(R.id.auto_message, text);
        contentView.setTextViewText(R.id.auto_date, date);
        builder.setSmallIcon(R.drawable.ic_alarm_white_24dp);
        builder.setAutoCancel(true);
        builder.setOngoing(true);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setOnlyAlertOnce(true);
        builder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
        builder.setContent(contentView);
//        builder.setContentIntent(pendingIntent);
        builder.addAction(R.drawable.ic_launcher_foreground, "YES", pendingIntent1 );
        builder.addAction(R.drawable.ic_launcher_foreground, "NO", pendingIntent2);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());

//        Notification notification = builder.build();
//        notificationManager.notify(1,notification);
//        NotificationManager manager = (NotificationManager) context.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
//        manager.cancelAll();

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


}
