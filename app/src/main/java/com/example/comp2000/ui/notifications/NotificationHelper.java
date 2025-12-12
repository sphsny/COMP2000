package com.example.comp2000.ui.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.comp2000.MainActivity;
import com.example.comp2000.R;

public class NotificationHelper {

    private static final String channelID = "myChannel"; // set channel name

    // send notification function
    public static void sendNotification(Context context, String title, String text) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = notificationManager.getNotificationChannel(channelID);

            // create new channel
            if (channel == null) {
                channel = new NotificationChannel(channelID, "Booking Notifications", NotificationManager.IMPORTANCE_HIGH);
                channel.setLightColor(Color.BLUE);
                channel.enableVibration(true);

                notificationManager.createNotificationChannel(channel);
            }

        // open main activity upon tapping on notification
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_MUTABLE);

        // build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(title) // custom title to be passed in
                .setContentText(text) // custom text to be passed in
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // show the notification with unique ID
        notificationManager.notify(0, builder.build());
        }
    }
}

// reference: week 9 recording