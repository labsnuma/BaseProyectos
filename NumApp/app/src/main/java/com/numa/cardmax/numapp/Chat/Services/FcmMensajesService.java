package com.numa.cardmax.numapp.Chat.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.numa.cardmax.numapp.Chat.NotifiacionesActivity;
import com.numa.cardmax.numapp.R;

public class FcmMensajesService extends FirebaseMessagingService {

    private static final String MENSAJE = "tiempo";
    private int tiemponumero = 4;

    public FcmMensajesService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0 && remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        float desc = Float.valueOf(remoteMessage.getData().get(MENSAJE));
        // String desc = String.valueOf(remoteMessage.getData().get(MENSAJE));

        Intent intent = new Intent(this, NotifiacionesActivity.class);
        intent.putExtra(MENSAJE, desc);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultsonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder nottifibuild = new Notification.Builder(this).
                setSmallIcon(R.drawable.prueba).
                setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(defaultsonido)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nottifibuild.setColor(desc >= tiemponumero ?
                    ContextCompat.getColor(getApplicationContext(), R.color.colorHintTab) :
                    ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String canal = getString(R.string.app_name);
            String nombre = getString(R.string.apellidos);
            NotificationChannel canalmodificacdo =
                    new NotificationChannel(canal, nombre,
                            NotificationManager.IMPORTANCE_DEFAULT);
            canalmodificacdo.enableVibration(true);
            canalmodificacdo.setVibrationPattern(new long[]{100, 200, 50});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(canalmodificacdo);
            }
            nottifibuild.setChannelId(canal);
        }

        if (notificationManager != null) {
            notificationManager.notify("", 0, nottifibuild.build());
        } else {

        }

    }


}
