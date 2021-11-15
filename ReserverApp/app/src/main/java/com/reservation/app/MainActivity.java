package com.reservation.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.reservation.app.ui.Login;
import com.reservation.app.viewmodel.AppViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private AppViewModel appViewModel;
    private static int TIME1 = 5000;
    TextView logoText1,logoText2;
    private CircleImageView splashImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(AppViewModel.class);
        appViewModel.initUserData();
        appViewModel.initUserProfilePicture();
        splashImage = findViewById(R.id.imageView6);

        logoText1 = findViewById(R.id.reserver_app_logo);
        logoText2 = findViewById(R.id.textView8);

        YoYo.with(Techniques.FadeIn).delay(100).duration(850).repeat(0).playOn(logoText1);
        YoYo.with(Techniques.FadeIn).delay(100).duration(850).repeat(0).playOn(logoText2);
        YoYo.with(Techniques.FadeOut).delay(4500).duration(800).repeat(0).playOn(logoText1);
        YoYo.with(Techniques.FadeOut).delay(4500).duration(800).repeat(0).playOn(logoText2);





        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        }, TIME1);
    }
}