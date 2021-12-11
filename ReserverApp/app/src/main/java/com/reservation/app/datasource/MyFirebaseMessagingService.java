package com.reservation.app.datasource;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.reservation.app.R;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;



@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "myFirebaseChannel",
                    "appNotification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        getFirebaseMessage(Objects.requireNonNull(remoteMessage.getNotification()).getTitle()
                ,remoteMessage.getNotification().getBody());


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getFirebaseMessage(String title, String message){




        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"myFirebaseChannel")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);


        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(499,builder.build());

    }






}
