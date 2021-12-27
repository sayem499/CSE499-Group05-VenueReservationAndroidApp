package com.reservation.app.ui.venue.booking;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reservation.app.R;
import com.reservation.app.datasource.VenueDataManager;
import com.reservation.app.datasource.helper.RemoteResult;
import com.reservation.app.model.BookingInfo;
import com.reservation.app.ui.util.DialogBuilder;
import com.reservation.app.ui.venue.booking.adapter.BookingListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VenueBookingListActivity extends AppCompatActivity {

    private RecyclerView bookingList;
    private BookingListAdapter bookingListAdapter;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_booking_list);

        bookingList = findViewById(R.id.booking_list);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        configureBookingList();

        if (firebaseUser != null) {
            requestBookingList(firebaseUser.getPhoneNumber());
        }

        setSupportActionBar(findViewById(R.id.toolbar_booking_list));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestBookingList(String userId) {
        VenueDataManager.requestBookingList(userId, new RemoteResult<List<BookingInfo>>() {

            @Override
            public void onSuccess(List<BookingInfo> data) {
                bookingListAdapter.updateList(data);
            }

            @Override
            public void onFailure(Exception error) {
                DialogBuilder.buildOkDialog(VenueBookingListActivity.this, error.getMessage()).show();
            }
        });
    }

    private void configureBookingList() {
        bookingListAdapter = new BookingListAdapter(new ArrayList<>());
        bookingList.setLayoutManager(new LinearLayoutManager(this));
        bookingList.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        bookingList.setAdapter(bookingListAdapter);
    }
}