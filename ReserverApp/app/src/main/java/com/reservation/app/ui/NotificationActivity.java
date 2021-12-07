package com.reservation.app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.reservation.app.R;

import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = findViewById(R.id.notification_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}