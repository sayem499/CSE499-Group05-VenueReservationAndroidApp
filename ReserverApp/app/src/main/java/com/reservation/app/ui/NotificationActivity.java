package com.reservation.app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.reservation.app.R;

import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    private FloatingActionButton notificationSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = findViewById(R.id.notification_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        notificationSender = findViewById(R.id.notificationSender_floatingActionButton);

        notificationSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NotificationSenderActivity.class));
            }
        });
    }
}