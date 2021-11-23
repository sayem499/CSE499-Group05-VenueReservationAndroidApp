package com.reservation.app.ui.venue.booking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.reservation.app.R;

public class VenueBookingActivity extends AppCompatActivity {

    public static final String EXTRA_VENUE = "extra_venue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_booking);
    }
}