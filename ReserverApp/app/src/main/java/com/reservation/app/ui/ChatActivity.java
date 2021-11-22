package com.reservation.app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.reservation.app.R;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }
}