package com.reservation.app.ui.venue.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.reservation.app.R;
import com.reservation.app.model.Venue;

public class VenueBookingActivity extends AppCompatActivity {

    public static final String EXTRA_VENUE = "extra_venue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_booking);

        Intent intent = getIntent();

        Venue venue = (Venue) intent.getSerializableExtra(EXTRA_VENUE);
    }
}