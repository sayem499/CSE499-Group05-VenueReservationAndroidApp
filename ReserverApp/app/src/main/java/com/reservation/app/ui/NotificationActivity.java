package com.reservation.app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.reservation.app.R;
import com.reservation.app.adapters.NotificationAdapter;
import com.reservation.app.datasource.NotificationModel;
import com.reservation.app.datasource.SharedPrefManager;
import com.reservation.app.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    private FloatingActionButton notificationSender;
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private AppViewModel appViewModel;
    private List<NotificationModel> notificationModelList;
    private SharedPrefManager pref;
    private static final String LOG_TAG = NotificationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = findViewById(R.id.notification_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Log.d(LOG_TAG,"!AAAAAAAAAAAFter PREFF ");
        appViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(AppViewModel.class);

        pref = new SharedPrefManager(NotificationActivity.this);

        notificationModelList = new ArrayList<>();
        appViewModel.initNotifications(this);
        notificationModelList = appViewModel.fetchNotifications();


        notificationSender = findViewById(R.id.notificationSender_floatingActionButton);

        notificationSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NotificationSenderActivity.class));
            }
        });

        recyclerView = findViewById(R.id.notificationList_recyclerView);
        notificationAdapter = new NotificationAdapter(new ArrayList<>(),NotificationActivity.this);
        recyclerView.setAdapter(notificationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));

        if(notificationModelList != null){
            Log.d(LOG_TAG,"!INSIDE  IFFF!!!!!!!!!!!!");
             notificationAdapter.setNotificationList(notificationModelList);

        }
    }
}