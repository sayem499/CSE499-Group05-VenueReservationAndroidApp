package com.reservation.app.datasource;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.reservation.app.MainActivity;
import com.reservation.app.R;

import com.reservation.app.viewmodel.AppViewModel;


import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
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

        if(remoteMessage.getData().isEmpty()) {
            getFirebaseMessage(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(),
                    remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getImageUrl());
        }else{

            getFirebaseMessage(remoteMessage.getData());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getFirebaseMessage( String title, String message, Uri imageUri){
        /*appViewModel = new ViewModelProvider((ViewModelStoreOwner) this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(AppViewModel.class);
        pref = new SharedPrefManager(getApplicationContext());
        final String[] userImageURL = new String[1];
        NotificationModel notificationModel = new NotificationModel();
        if(imageUri != null){
            storageReference = FirebaseStorage.getInstance().getReference().child("Notification Pic");
            final StorageReference fileRef = storageReference.child(pref.getPhoneNumber()+System.currentTimeMillis()+".jpg" );
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull @NotNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull @NotNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl = (Uri) task.getResult();
                        assert downloadUrl != null;
                        userImageURL[0] = downloadUrl.toString();
                        notificationModel.setNotificationImageUrl(userImageURL[0]);


                    }
                }
            });
        }



        notificationModel.setNotificationTitle(title);
        notificationModel.setNotificationMessage(message);
        notificationModel.setUserPhoneNumber(pref.getPhoneNumber());


            appViewModel.insertNotification(notificationModel,getApplicationContext());*/



        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"myFirebaseChannel")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX);
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(499,builder.build());





    }

    private void getFirebaseMessage(@NotNull Map<String, String> data){

        Bitmap bitmap;

        String title = Objects.requireNonNull(data.get("title")).toString();
        String body = Objects.requireNonNull(data.get("body")).toString();
        String imageUri = Objects.requireNonNull(data.get("image")).toString();
        
        bitmap = getBitmapFromUrl(imageUri);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"myFirebaseChannel")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_MAX);

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(499,builder.build());

    }

    private Bitmap getBitmapFromUrl(String imageUri) {

        try {

            URL url = new URL(imageUri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }


    }


}
