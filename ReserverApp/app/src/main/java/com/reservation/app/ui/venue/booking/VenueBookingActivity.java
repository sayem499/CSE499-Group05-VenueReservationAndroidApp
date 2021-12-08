package com.reservation.app.ui.venue.booking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reservation.app.R;
import com.reservation.app.databinding.ActivityVenueBookingBinding;
import com.reservation.app.datasource.VenueDataManager;
import com.reservation.app.datasource.helper.RemoteResult;
import com.reservation.app.model.BookingInfo;
import com.reservation.app.model.Venue;
import com.reservation.app.ui.util.DialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class VenueBookingActivity extends AppCompatActivity {

    public static final String EXTRA_VENUE = "extra_venue";

    private ActivityVenueBookingBinding viewBinding;

    private Venue venue;
    private Date selectedDate;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = ActivityVenueBookingBinding.inflate(getLayoutInflater());

        setContentView(viewBinding.getRoot());

        Intent intent = getIntent();

        venue = (Venue) intent.getSerializableExtra(EXTRA_VENUE);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        listenDateInput();
        listenBookingButton();

        updateVenueUi();
    }

    private void listenDateInput() {
        viewBinding.dateInput.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        viewBinding.dateShow.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        calendar.set(mYear, mMonth, mYear);
                        selectedDate = calendar.getTime();
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });
    }

    private void listenBookingButton() {
        viewBinding.booking.setOnClickListener(v -> {
            int index = viewBinding.venueSlot.getSelectedIndex();
            if (index < 0) {
                DialogBuilder.buildOkDialog(this, "Please select the venue slot").show();
            } else if (viewBinding.dateShow.getText().toString().equals("DD/MM/YYYY")) {
                DialogBuilder.buildOkDialog(this, "Please select date").show();
            } else {
                bookVenue();
            }
        });
    }

    private boolean bookVenue() {
        //update bookedSlots
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setSlot(getSelectedSlot());
        bookingInfo.setVenueId(venue.getId());
        bookingInfo.setDate(selectedDate);

        if (firebaseUser != null) {
            bookingInfo.setUserId(firebaseUser.getPhoneNumber());
        }

        VenueDataManager.saveBookingInfo(bookingInfo, new RemoteResult<BookingInfo>() {
            @Override
            public void onSuccess(BookingInfo data) {
                Dialog dialog = DialogBuilder.buildOkDialog(VenueBookingActivity.this, "Booking is Successful",
                        (d, which) -> {
                            finish();
                        }
                );
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            public void onFailure(Exception error) {
                Toast.makeText(VenueBookingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return true;
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