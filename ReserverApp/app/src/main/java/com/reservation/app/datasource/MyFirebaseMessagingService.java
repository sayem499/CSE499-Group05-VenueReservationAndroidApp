package com.reservation.app.datasource;


import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.reservation.app.R;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;



@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        getFirebaseMessage(Objects.requireNonNull(remoteMessage.getNotification()).getTitle()
                ,remoteMessage.getNotification().getBody());


    }

    public void getFirebaseMessage(String title,String message){


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"myFirebaseChannel")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);


        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(499,builder.build());

    }






}
