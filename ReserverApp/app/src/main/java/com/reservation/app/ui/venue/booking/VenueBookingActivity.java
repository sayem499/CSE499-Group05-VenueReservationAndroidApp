package com.reservation.app.ui.venue.booking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.reservation.app.R;
import com.reservation.app.databinding.ActivityVenueBookingBinding;
import com.reservation.app.model.Venue;
import com.reservation.app.ui.util.DialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

public class VenueBookingActivity extends AppCompatActivity {

    public static final String EXTRA_VENUE = "extra_venue";

    private ActivityVenueBookingBinding viewBinding;

    private Venue venue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = ActivityVenueBookingBinding.inflate(getLayoutInflater());

        setContentView(viewBinding.getRoot());

        Intent intent = getIntent();

        venue = (Venue) intent.getSerializableExtra(EXTRA_VENUE);

        listenBookingButton();
        updateVenueUi();
    }

    private void listenBookingButton() {
        viewBinding.booking.setOnClickListener(v -> {
            int index = viewBinding.venueSlot.getSelectedIndex();
            if (index < 0) {
                DialogBuilder.buildOkDialog(this, "Please select the venue slot").show();
            }
        });
    }

    private void updateVenueUi() {
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

    private String getSelectedSlot() {
        int index = viewBinding.venueSlot.getSelectedIndex();

        String[] slots = getResources().getStringArray(R.array.booking_slot);

        return slots[index];
    }

}