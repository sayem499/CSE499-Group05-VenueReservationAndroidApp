package com.reservation.app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;

import com.reservation.app.R;

public class Profile extends AppCompatActivity {

    DialogFragment dialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void launchEditProfileFragment(View view) {
        dialogFragment = EditProfileFragment.newInstance("ON","OFF");
        dialogFragment.show(getSupportFragmentManager(),"Edit Profile");

    }
}