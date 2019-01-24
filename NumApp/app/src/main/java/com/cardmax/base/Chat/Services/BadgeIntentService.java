package com.cardmax.base.Chat.Services;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.cardmax.base.Chat.InicioActivity;
import com.cardmax.base.R;


import me.leolin.shortcutbadger.ShortcutBadger;


public class BadgeIntentService extends IntentService {

    private static final String NOTIFICATION_CHANNEL = "me.leolin.shortcutbadger.example";

    private int notificationId = 0;

    public BadgeIntentService() {
        super("BadgeIntentService");
    }

    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int badgeCount = intent.getIntExtra("badgeCount", 0);

            mNotificationManager.cancel(notificationId);
            notificationId++;


            Intent resultIntent=new Intent(this,InicioActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);


            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            Uri defaultsonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Solicitud de Amistad")
                    .setContentText("Sebastian Parra"+" quiere ser tu amigo.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(defaultsonido)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupNotificationChannel();

                builder.setChannelId(NOTIFICATION_CHANNEL);
            }

            Notification notification = builder.build();
            ShortcutBadger.applyNotification(getApplicationContext(), notification, badgeCount);
            mNotificationManager.notify(notificationId, notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void setupNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, "ShortcutBadger Sample",
                NotificationManager.IMPORTANCE_DEFAULT);

        mNotificationManager.createNotificationChannel(channel);
    }
}