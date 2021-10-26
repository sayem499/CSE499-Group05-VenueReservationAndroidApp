package com.reservation.app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.reservation.app.R;
import com.reservation.app.datasource.SharedPrefManager;
import com.reservation.app.datasource.UserModel;
import com.reservation.app.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    DialogFragment dialogFragment;
    private EditText mobileNumber,email,address;
    private TextView userName;
    private List<UserModel> userList;
    private SharedPrefManager pref;
    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        appViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(AppViewModel.class);
        pref = new SharedPrefManager(Profile.this);

        userName = findViewById(R.id.username_textView);
        mobileNumber = findViewById(R.id.editText_profile_phone);
        email = findViewById(R.id.editText_profile_email);
        address = findViewById(R.id.editText_profile_address);

        userList = new ArrayList<>();
        appViewModel.initUserData();
        userList = appViewModel.fetchUserData();
        setUserData();


    }

    @SuppressLint("SetTextI18n")
    private void setUserData() {
        if(userList != null)
            for(UserModel userModel : userList)
                if(userModel.getUserPhoneNumber().equals(pref.getPhoneNumber())){

                    userName.setText(userModel.getUserFirstName() +" "+ userModel.getUserLastName());
                    email.setText(userModel.getUserEmail());
                    address.setText(userModel.getUserAddress());
                    mobileNumber.setText(userModel.getUserPhoneNumber());
                    break;
                }

    }

    public void launchEditProfileFragment(View view) {
        dialogFragment = EditProfileFragment.newInstance("ON","OFF");
        dialogFragment.show(getSupportFragmentManager(),"Edit Profile");

    }
}