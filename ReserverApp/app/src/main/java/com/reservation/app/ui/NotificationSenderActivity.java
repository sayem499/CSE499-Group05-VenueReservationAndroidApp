package com.reservation.app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.reservation.app.R;
import com.reservation.app.datasource.FcmNotificationsSender;
import com.reservation.app.datasource.NotificationModel;
import com.reservation.app.datasource.SharedPrefManager;
import com.reservation.app.viewmodel.AppViewModel;

import java.util.Calendar;
import java.util.Objects;

public class NotificationSenderActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;
    private EditText notificationTitle,notificationMessage,notificationImageUrl;
    private Button notificationSend;
    private SharedPrefManager pref;
    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_sender);

        toolbar = findViewById(R.id.notification_sender_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        notificationTitle = findViewById(R.id.notificationTitle_editText);
        notificationMessage = findViewById(R.id.notificationMessage_editText);
        notificationImageUrl = findViewById(R.id.notificationImageUrl_editText);
        notificationSend = findViewById(R.id.notificationSend_button);

        notificationSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!notificationTitle.getText().toString().isEmpty() && !notificationMessage.getText().toString().isEmpty()){

                    FcmNotificationsSender notificationsSender =
                            new FcmNotificationsSender("/topics/notify",notificationTitle.getText().toString()
                                    ,notificationMessage.getText().toString(),notificationImageUrl.getText().toString(),getApplicationContext(),NotificationSenderActivity.this);
                    appViewModel = new ViewModelProvider(NotificationSenderActivity.this, ViewModelProvider.AndroidViewModelFactory
                            .getInstance(getApplication())).get(AppViewModel.class);
                    pref = new SharedPrefManager(NotificationSenderActivity.this);
                    NotificationModel notificationModel = new NotificationModel();
                    notificationModel.setUserPhoneNumber(pref.getPhoneNumber());
                    notificationModel.setNotificationTitle(notificationTitle.getText().toString());
                    notificationModel.setNotificationMessage(notificationMessage.getText().toString());
                    notificationModel.setNotificationImageUrl(notificationImageUrl.getText().toString());
                    notificationModel.setNotificationTime(String.valueOf(Calendar.getInstance().getTime()));

                    appViewModel.insertNotification(notificationModel,NotificationSenderActivity.this);

                    notificationsSender.SendNotifications();

                } else{

                    Toast.makeText(NotificationSenderActivity.this,"Fill out the necessary fields !!",Toast.LENGTH_SHORT).show();
                }


            }
        });



    }
}