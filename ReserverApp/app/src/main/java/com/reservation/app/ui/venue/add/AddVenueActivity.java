package com.reservation.app.ui.venue.add;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.reservation.app.R;
import com.reservation.app.datasource.UserProfilePicture;
import com.reservation.app.datasource.VenueDataManager;
import com.reservation.app.datasource.helper.RemoteResult;
import com.reservation.app.model.Address;
import com.reservation.app.model.Venue;
import com.reservation.app.ui.Profile;
import com.reservation.app.ui.util.DialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @author Fatema
 * since 8/25/21.
 */
public class AddVenueActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edtVenueName;
    private EditText edtVenueDescription;
    private EditText edtRentPrice;
    private EditText edtCapacity;
    private EditText edtStreet1;
    private EditText edtStreet2;
    private EditText edtCity;
    private EditText edtPostCode;
    private Button btnSaveVenue;
    private FloatingActionButton floatingActionButton;
    private ImageView imageViewVenue;
    private Uri venueImageUri;
    private String venueImageURL = "";
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);

        edtVenueName = findViewById(R.id.edt_venue_name);
        edtVenueDescription = findViewById(R.id.edt_venue_description);
        edtRentPrice = findViewById(R.id.edt_venue_price);
        edtCapacity = findViewById(R.id.edt_venue_capacity);
        edtStreet1 = findViewById(R.id.edt_street1);
        edtStreet2 = findViewById(R.id.edt_street2);
        edtCity = findViewById(R.id.edt_city);
        edtPostCode = findViewById(R.id.edt_postcode);
        btnSaveVenue = findViewById(R.id.save_venue);
        floatingActionButton = findViewById(R.id.add_venue_photo);
        imageViewVenue = findViewById(R.id.venue_photo);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btnSaveVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVenue();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddVenueActivity.this)
                        .crop()//Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        toolbar = findViewById(R.id.add_venue_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            assert data != null;
            venueImageUri = data.getData();
            uploadVenuePhoto();
            imageViewVenue.setImageURI(venueImageUri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadVenuePhoto() {
        final Dialog progressDialog = DialogBuilder.buildProgressDialog(this, "Uploading image");
        progressDialog.show();

        Random random = new Random();

        if (venueImageUri != null && firebaseUser != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Venue Pic");
            final StorageReference fileRef = storageReference.child(firebaseUser.getPhoneNumber() + random.nextInt() + ".jpg");
            StorageTask uploadTask = fileRef.putFile(venueImageUri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileRef.getDownloadUrl();

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        venueImageURL = downloadUrl.toString();

                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Toast.makeText(AddVenueActivity.this, "Image not selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveVenue() {
        Venue venue = new Venue();
        venue.setName(edtVenueName.getText().toString());
        venue.setDescription(edtVenueDescription.getText().toString());
        try {
            venue.setRentCost(Double.parseDouble(edtRentPrice.getText().toString()));
        } catch (Exception ignored) {
        }

        try {
            venue.setSeatCapacity(Integer.parseInt(edtCapacity.getText().toString()));
        } catch (Exception ignored) {
        }

        Address address = new Address();
        address.setStreet1(edtStreet1.getText().toString());
        address.setStreet2(edtStreet2.getText().toString());
        address.setCity(edtCity.getText().toString());
        address.setPostCode(edtPostCode.getText().toString());

        venue.setAddress(address);

        if (firebaseUser != null) {
            venue.setUserPhoneNumber(firebaseUser.getPhoneNumber());
        }
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add(venueImageURL);
        venue.setPhotoUrls(photoUrls);

        Dialog progressDialog = DialogBuilder.buildProgressDialog(this, "Saving...");
        progressDialog.show();

        VenueDataManager.saveVenue(venue, new RemoteResult<Venue>() {
            @Override
            public void onSuccess(Venue data) {
                progressDialog.dismiss();
                Toast.makeText(AddVenueActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Exception error) {
                progressDialog.dismiss();
                Toast.makeText(AddVenueActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}