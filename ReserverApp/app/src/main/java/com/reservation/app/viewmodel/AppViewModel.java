package com.reservation.app.viewmodel;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.reservation.app.datasource.NotificationModel;
import com.reservation.app.datasource.UserModel;
import com.reservation.app.datasource.UserProfilePicture;
import com.reservation.app.repository.AppRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private final AppRepository appRepository;
    private List<UserModel> userList;
    private List<UserProfilePicture> userProfilePictureList;
    private List<NotificationModel> notificationList;

    public AppViewModel(@NonNull @NotNull Application application) {
        super(application);

        appRepository = AppRepository.getInstance();
    }

    public void insertUserData(UserModel userModel, Context context){
        appRepository.insertUserData(userModel,context);
    }


    public void initUserData(){
        if(userList != null){
            return;
        }
        userList = appRepository.fetchUserData();
    }

    public List<UserModel> fetchUserData() {
        return userList;
    }

    public void insertProfilePic(UserProfilePicture userProfilePicture,Context context){
        appRepository.insertProfilePic(userProfilePicture,context);
    }

    public void initUserProfilePicture(){
        if(userProfilePictureList != null){
            return;
        }
        userProfilePictureList = appRepository.fetchUserProfilePicture();
    }

    public List<UserProfilePicture> fetchUserProfilePictureList(){return userProfilePictureList;}

    public void insertNotification(NotificationModel notificationModel, Context context){
        appRepository.insertNotification(notificationModel,context);
    }

    public void initNotifications(Context context){
        if(notificationList != null){
            return;
        }
        notificationList = appRepository.fetchNotifications(context);
    }

    public List<NotificationModel> fetchNotifications(){return notificationList;}

}
