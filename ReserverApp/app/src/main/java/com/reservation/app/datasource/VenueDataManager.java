package com.reservation.app.datasource;

import static com.reservation.app.ui.venue.booking.VenueBookingActivity.DAY_SHIFT;
import static com.reservation.app.ui.venue.booking.VenueBookingActivity.NIGHT_SHIFT;
import static com.reservation.app.util.DateTimeUtils.checkDate;
import static com.reservation.app.util.DateTimeUtils.dateToCalendar;
import static com.reservation.app.util.DateTimeUtils.dateToStr;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reservation.app.datasource.helper.RemoteResult;
import com.reservation.app.model.BookingInfo;
import com.reservation.app.model.Venue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Fatema
 * since 9/1/21.
 */
public class VenueDataManager {

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference venueRef = database.getReference("venues");
    private static final DatabaseReference bookingRef = database.getReference("bookings");

    static public void requestVenueList(RemoteResult<List<Venue>> resultCallback) {

        venueRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                resultCallback.onFailure(task.getException());

                Log.e("firebase", "Error getting data", task.getException());
            } else {
                if (task.getResult() == null) {
                    resultCallback.onFailure(new Exception("Not receive formatted result."));
                    return;
                }
                List<Venue> venues = new ArrayList<>();

                for (DataSnapshot venueTask : task.getResult().getChildren()) {
                    Venue venue = venueTask.getValue(Venue.class);

                    if (venue != null) {
                        venue.setId(venueTask.getKey());
                        venues.add(venue);
                    }
                }
                resultCallback.onSuccess(venues);
                Log.d("firebase", String.valueOf(task.getResult().getValue()));
            }
        });
    }

    public static void saveVenue(Venue venue, RemoteResult<Venue> resultCallBack) {

        venueRef.push().setValue(venue).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("firebase", String.valueOf(task.getResult()));
                resultCallBack.onFailure(task.getException());

            } else {
                resultCallBack.onSuccess(venue);
            }
        });
    }

    public static void saveBookingInfo(BookingInfo bookingInfo, RemoteResult<BookingInfo> resultCallBack) {

        bookingRef.push().setValue(bookingInfo).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("firebase", String.valueOf(task.getResult()));
                resultCallBack.onFailure(task.getException());

            } else {
                resultCallBack.onSuccess(bookingInfo);
            }
        });
    }

    static public void requestBookingList(String userId, RemoteResult<List<BookingInfo>> resultCallback) {

        bookingRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                resultCallback.onFailure(task.getException());

                Log.e("firebase", "Error getting data", task.getException());
            } else {
                if (task.getResult() == null) {
                    resultCallback.onFailure(new Exception("Not receive formatted result."));
                    return;
                }
                List<BookingInfo> bookingInfoList = new ArrayList<>();

                for (DataSnapshot venueTask : task.getResult().getChildren()) {
                    BookingInfo bookingInfo = venueTask.getValue(BookingInfo.class);

                    if (bookingInfo != null && bookingInfo.getUserId().equals(userId)) {
                        bookingInfoList.add(bookingInfo);
                    }
                }
                resultCallback.onSuccess(bookingInfoList);
                Log.d("firebase", String.valueOf(task.getResult().getValue()));
            }
        });
    }

    static public void requestBookedSlot(String venueId, Date date, RemoteResult<List<String>> resultCallback) {

        bookingRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                resultCallback.onFailure(task.getException());

                Log.e("firebase", "Error getting data", task.getException());
            } else {
                if (task.getResult() == null) {
                    resultCallback.onFailure(new Exception("Not receive formatted result."));
                    return;
                }
                List<String> bookedSlots = new ArrayList<>();
                bookedSlots.add(DAY_SHIFT);
                bookedSlots.add(NIGHT_SHIFT);

                for (DataSnapshot venueTask : task.getResult().getChildren()) {
                    BookingInfo bookingInfo = venueTask.getValue(BookingInfo.class);

                    if (bookingInfo != null
                            && bookingInfo.getVenueId().equals(venueId)
                            && checkDate(bookingInfo.getDate(), date)) {
                        bookedSlots.remove(bookingInfo.getSlot());
                    }
                }
                resultCallback.onSuccess(bookedSlots);
                Log.d("firebase", String.valueOf(task.getResult().getValue()));
            }
        });
    }

    static public void requestBookedDates(String venueId, RemoteResult<List<Calendar>> resultCallback) {

        bookingRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                resultCallback.onFailure(task.getException());

                Log.e("firebase", "Error getting data", task.getException());
            } else {
                if (task.getResult() == null) {
                    resultCallback.onFailure(new Exception("Not receive formatted result."));
                    return;
                }
                List<Calendar> bookedDates = new ArrayList<>();
                Set<String> slots = new HashSet<>();

                for (DataSnapshot venueTask : task.getResult().getChildren()) {
                    BookingInfo bookingInfo = venueTask.getValue(BookingInfo.class);

                    if (bookingInfo != null && bookingInfo.getVenueId().equals(venueId)) {
                        String dateStr = dateToStr(bookingInfo.getDate());

                        if (slots.contains(dateStr)) {
                            bookedDates.add(dateToCalendar(bookingInfo.getDate()));
                        } else {
                            slots.add(dateStr);
                        }
                    }
                }
                resultCallback.onSuccess(bookedDates);
                Log.d("firebase", String.valueOf(task.getResult().getValue()));
            }
        });
    }

}
