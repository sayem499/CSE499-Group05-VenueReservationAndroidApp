package com.reservation.app.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.reservation.app.R;
import com.reservation.app.datasource.SharedPrefManager;
import com.reservation.app.datasource.UserModel;
import com.reservation.app.datasource.UserProfilePicture;
import com.reservation.app.viewmodel.AppViewModel;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;
    DialogFragment dialogFragment;
    private EditText mobileNumber,email,address;
    private TextView userName;
    private List<UserModel> userList;
    private List<UserProfilePicture> userProfilePicList;
    private SharedPrefManager pref;
    private AppViewModel appViewModel;
    private CircleImageView userImage;
    private FloatingActionButton floatingActionButton;
    private Uri userImageUri;
    private String userImageURL = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private static final String LOG_TAG = Profile.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);




        appViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(AppViewModel.class);
        pref = new SharedPrefManager(Profile.this);

        userImage = findViewById(R.id.userImage);
        floatingActionButton = findViewById(R.id.floatingActionButton_userImage);

        userName = findViewById(R.id.username_textView);
        mobileNumber = findViewById(R.id.editText_profile_phone);
        email = findViewById(R.id.editText_profile_email);
        address = findViewById(R.id.editText_profile_address);

        userProfilePicList = new ArrayList<>();
        appViewModel.initUserProfilePicture();
        userProfilePicList = appViewModel.fetchUserProfilePictureList();
        setProfilePic();

        userList = new ArrayList<>();
        appViewModel.initUserData();
        userList = appViewModel.fetchUserData();
        setUserData();



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Profile.this)
                        .crop()//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        assert data != null;
        userImageUri = data.getData();
        uploadProfilePicture();
        userImage.setImageURI(userImageUri);

    }

    private void setProfilePic() {
        if(userProfilePicList != null) {
            for (UserProfilePicture userProfilePicture : userProfilePicList) {
                Log.d(LOG_TAG,"!INSIDE  IFFF!!!!!!!!!!!!");
                if (userProfilePicture.getUserPhoneNumber().equals(pref.getPhoneNumber())) {

                    Glide.with(Profile.this).load(userProfilePicture.getPictureUrl()).into(userImage);
                    break;
                }

            }
        }

    }

    private void uploadProfilePicture() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile Picture Upload.");
        progressDialog.setMessage("Please wait, while we are setting your data.");
        progressDialog.show();

        if(userImageUri != null){
            storageReference = FirebaseStorage.getInstance().getReference().child("Profile Pic");
            final StorageReference fileRef = storageReference.child(pref.getPhoneNumber()+".jpg");
            uploadTask = fileRef.putFile(userImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull @NotNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl =  task.getResult();
                        userImageURL = downloadUrl.toString();
                        UserProfilePicture userProfilePicture = new UserProfilePicture();
                        userProfilePicture.setPictureUrl(userImageURL);
                        userProfilePicture.setUserPhoneNumber(pref.getPhoneNumber());

                        appViewModel.insertProfilePic(userProfilePicture,Profile.this);

                        progressDialog.dismiss();
                    }
                }
            });
        }else{
            Toast.makeText(Profile.this,"Image not selected.",Toast.LENGTH_SHORT).show();
        }
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