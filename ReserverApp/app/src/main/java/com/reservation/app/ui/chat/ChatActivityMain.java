package com.reservation.app.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.reservation.app.R;

import java.util.Objects;

public class ChatActivityMain extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        toolbar = findViewById(R.id.chat_main_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}