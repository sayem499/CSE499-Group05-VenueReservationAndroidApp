package com.reservation.app.ui.venue.booking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.reservation.app.R;
import com.reservation.app.databinding.ActivityVenueBookingBinding;
import com.reservation.app.model.Venue;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

public class VenueBookingActivity extends AppCompatActivity {

    public static final String EXTRA_VENUE = "extra_venue";

    private ActivityVenueBookingBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = ActivityVenueBookingBinding.inflate(getLayoutInflater());

        setContentView(viewBinding.getRoot());

        Intent intent = getIntent();

        Venue venue = (Venue) intent.getSerializableExtra(EXTRA_VENUE);

        updateVenueUi(venue);
    }

    private void updateVenueUi(Venue venue) {
        viewBinding.title.setText(venue.getName());
        viewBinding.address.setText(venue.getAddress().getDisplayAddressForList());
        viewBinding.price.setText(venue.getPrice());
        viewBinding.capacity.setText(venue.getFormattedSeatCapacity());
        viewBinding.description.setText(venue.getDescription());

        String photoPath = venue.getPhotoUrls().get(0);

        if (!TextUtils.isEmpty(photoPath)) {
            Picasso.get()
                    .load(photoPath)
                    .placeholder(R.drawable.progress_indicator)
                    .error(R.drawable.placeholder1)
                    .into(viewBinding.photo);
        }
    }

}