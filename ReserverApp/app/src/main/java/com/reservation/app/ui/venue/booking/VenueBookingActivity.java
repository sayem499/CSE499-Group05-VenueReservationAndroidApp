package com.reservation.app.ui.venue.booking;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.CollectionUtils;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VenueBookingActivity extends AppCompatActivity {

    public static final String EXTRA_VENUE = "extra_venue";
    public static final String DAY_SHIFT = "Day Shift";
    public static final String NIGHT_SHIFT = "Night Shift";

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
        selectedDate = Calendar.getInstance().getTime();

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

            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
                viewBinding.dateShow.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                calendar.set(year, monthOfYear, dayOfMonth);
                selectedDate = calendar.getTime();
                setVenueSlot();
            }, mYear, mMonth, mDay);
            datePickerDialog.setMinDate(Calendar.getInstance());
            setDisabledDays(datePickerDialog);
        });
    }

    private void setVenueSlot() {
        VenueDataManager.requestBookedSlot(venue.getId(), selectedDate, new RemoteResult<List<String>>() {

            @Override
            public void onSuccess(List<String> data) {
                viewBinding.venueSlot.setItems(data);
            }

            @Override
            public void onFailure(Exception error) {
                DialogBuilder.buildOkDialog(VenueBookingActivity.this, error.getMessage()).show();
            }
        });
    }

    private void setDisabledDays(DatePickerDialog datePickerDialog) {
        VenueDataManager.requestBookedDates(venue.getId(), new RemoteResult<List<Calendar>>() {

            @Override
            public void onSuccess(List<Calendar> data) {
                datePickerDialog.setDisabledDays(data.toArray(new Calendar[data.size()]));
                datePickerDialog.show(getSupportFragmentManager(), "Select Date");
            }

            @Override
            public void onFailure(Exception error) {
                DialogBuilder.buildOkDialog(VenueBookingActivity.this, error.getMessage()).show();
            }
        });
    }

    private void listenBookingButton() {
        viewBinding.booking.setOnClickListener(v -> {
            int index = viewBinding.venueSlot.getSelectedIndex();

            if (viewBinding.dateShow.getText().toString().equals("DD/MM/YYYY")) {
                DialogBuilder.buildOkDialog(this, "Please select date").show();
            } else if (index < 0) {
                DialogBuilder.buildOkDialog(this, "Please select the venue slot").show();
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
        bookingInfo.setTitle(venue.getName());
        bookingInfo.setDate(selectedDate);

        if (!CollectionUtils.isEmpty(venue.getPhotoUrls())) {
            bookingInfo.setPhotoUrl(venue.getPhotoUrls().get(0));
        }

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