package com.reservation.app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.reservation.app.MainActivity;
import com.reservation.app.R;
import com.reservation.app.datasource.FcmNotificationsSender;

import java.util.Objects;

public class NotificationSenderActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;
    private EditText notificationTitle,notificationMessage;
    private Button notificationSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_sender);

        toolbar = findViewById(R.id.notification_sender_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        notificationTitle = findViewById(R.id.notificationTitle_editText);
        notificationMessage = findViewById(R.id.notificationMessage_editText);
        notificationSend = findViewById(R.id.notificationSend_button);

        notificationSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!notificationTitle.getText().toString().isEmpty() && !notificationMessage.getText().toString().isEmpty()){

                    FcmNotificationsSender notificationsSender =
                            new FcmNotificationsSender("/topics/notify",notificationTitle.getText().toString()
                                    ,notificationMessage.getText().toString(),getApplicationContext(),NotificationSenderActivity.this);

                    notificationsSender.SendNotifications();

                } else{

                    Toast.makeText(NotificationSenderActivity.this,"Fill out the necessary fields !!",Toast.LENGTH_SHORT).show();
                }


            }
        });



    }
}