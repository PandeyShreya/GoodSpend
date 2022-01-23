package com.example.myapplication;

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
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String text = bundle.getString("event");
        String date = bundle.getString("date") + " " + bundle.getString("time");

        //When you click on notification
        Intent intent1 = new Intent(context, AlertsActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("message", text);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intent1,PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "FinanceManagerChannel");
//                .setSmallIcon(R.drawable.ic_alarm_white_24dp)
//                .setContentTitle("Alarm Manager")
//                .setContentText("Reminder")
//                .setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent);
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
       // contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);
        contentView.setTextViewText(R.id.message, text);
        contentView.setTextViewText(R.id.date, date);
        builder.setSmallIcon(R.drawable.ic_alarm_white_24dp);
        builder.setAutoCancel(true);
        builder.setOngoing(true);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setOnlyAlertOnce(true);
        builder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
        builder.setContent(contentView);
        builder.setContentIntent(pendingIntent);



        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());

//        Notification notification = builder.build();
//        notificationManager.notify(1,notification);
    }



}
