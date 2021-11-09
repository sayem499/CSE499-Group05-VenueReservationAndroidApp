package com.reservation.app.ui.venue.add;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reservation.app.R;
import com.reservation.app.datasource.VenueDataManager;
import com.reservation.app.datasource.helper.RemoteResult;
import com.reservation.app.model.Address;
import com.reservation.app.model.Venue;
import com.reservation.app.ui.util.DialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        btnSaveVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVenue();
            }
        });

        toolbar = findViewById(R.id.add_venue_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            venue.setUserPhoneNumber(firebaseUser.getPhoneNumber());
        }
        List<String> dummyPhotoUrls = new ArrayList<>();
        dummyPhotoUrls.add("https://firebasestorage.googleapis.com/v0/b/reserver-app-f3003.appspot.com/o/rondhonu_1.jpeg?alt=media&token=ff2c6bf5-3e6f-4680-9069-0ff2a4f9b68c");
        venue.setPhotoUrls(dummyPhotoUrls);

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