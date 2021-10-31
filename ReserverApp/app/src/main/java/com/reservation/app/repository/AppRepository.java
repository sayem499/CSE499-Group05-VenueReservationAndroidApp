package com.reservation.app.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reservation.app.datasource.SharedPrefManager;
import com.reservation.app.datasource.UserModel;
import com.reservation.app.datasource.UserProfilePicture;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AppRepository {
    private static AppRepository instance;
    private final FirebaseDatabase firebaseRef;
    private final DatabaseReference userDBRef,userProfilePicRef;
    private SharedPrefManager pref;

    private List<UserModel> userList;
    private List<UserProfilePicture> userProfilePictureList;

    public AppRepository(){
        firebaseRef = FirebaseDatabase.getInstance();
        userDBRef = firebaseRef.getReference("users");
        userProfilePicRef = firebaseRef.getReference("profile_pictures");


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
}
