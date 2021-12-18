package com.reservation.app.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reservation.app.datasource.NotificationModel;
import com.reservation.app.datasource.SharedPrefManager;
import com.reservation.app.datasource.UserModel;
import com.reservation.app.datasource.UserProfilePicture;
import com.reservation.app.ui.NotificationActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AppRepository {
    private static AppRepository instance;
    private final FirebaseDatabase firebaseRef;
    private DatabaseReference userDBRef,userProfilePicRef,notificationModelRef,getNotificationModelRef;
    private SharedPrefManager pref;

    private List<UserModel> userList;
    private List<UserProfilePicture> userProfilePictureList;
    private List<NotificationModel> notificationList;

    public AppRepository(){
        firebaseRef = FirebaseDatabase.getInstance();
        userDBRef = firebaseRef.getReference("users");
        userProfilePicRef = firebaseRef.getReference("profile_pictures");
        notificationModelRef = firebaseRef.getReference("notifications");



    }


    public static AppRepository getInstance(){
        if(instance == null){
            instance = new AppRepository();
        }
        return instance;
    }

    public void insertUserData(UserModel userModel, Context context){
        pref = new SharedPrefManager(context);
        userDBRef.child(pref.getPhoneNumber()).setValue(userModel);
    }

    public List<UserModel> fetchUserData() {
      userDBRef.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
              if(snapshot.exists()){
                  userList = new ArrayList<>();
                  for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                      userList.add(dataSnapshot.getValue(UserModel.class));
                  }
              }
          }

          @Override
          public void onCancelled(@NonNull @NotNull DatabaseError error) {

          }
      });

      return userList;
    }

    public void insertProfilePic(UserProfilePicture userProfilePicture,Context context){
        pref = new SharedPrefManager(context);
        userProfilePicRef.child(pref.getPhoneNumber()).setValue(userProfilePicture);
    }

    public List<UserProfilePicture> fetchUserProfilePicture(){
        userProfilePicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userProfilePictureList = new ArrayList<>();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        userProfilePictureList.add(dataSnapshot.getValue(UserProfilePicture.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return userProfilePictureList;
    }

    public void insertNotification(NotificationModel notificationModel,Context context){
        pref = new SharedPrefManager(context);
        notificationModelRef.child(pref.getPhoneNumber()).child(String.valueOf(Calendar.getInstance().getTime())).setValue(notificationModel);
    }

    public List<NotificationModel> fetchNotifications(Context context){
        pref = new SharedPrefManager(context);
        getNotificationModelRef = firebaseRef.getReference("notifications/"+pref.getPhoneNumber());
        getNotificationModelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    notificationList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        notificationList.add(dataSnapshot.getValue(NotificationModel.class));


                    }
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return notificationList;
    }
}


