package com.example.comp2000.ui.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp2000.ui.notifications.NotificationHelper;
import com.example.comp2000.R;
import com.example.comp2000.data.model.Booking;
import com.example.comp2000.data.model.RestaurantDB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StaffBookingFragment extends Fragment {

    private RestaurantDB db;
    private Calendar calendar;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private TextView dateHolder;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        // define view to inflate
        View view = inflater.inflate(R.layout.fragment_staff_booking, container, false);

        // connect SQLite DB
        db = new RestaurantDB(requireContext());

        // find views from XML file
        dateHolder = view.findViewById(R.id.dateHolder);
        Button leftBtn = view.findViewById(R.id.left_date_btn);
        Button rightBtn = view.findViewById(R.id.right_date_btn);

        // set up recyclerview
        recyclerView = view.findViewById(R.id.reservationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // get today's date
        calendar = Calendar.getInstance();
        updateDisplayedDate();
        loadBookings();

        // change date by clicking on arrow buttons
        // left button -> subtracts one day
        leftBtn.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            updateDisplayedDate(); // update date
            loadBookings(); // update bookings
        });


        // right button -> adds one day
        rightBtn.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            updateDisplayedDate();
            loadBookings();
        });

        return view;
    }

    private void updateDisplayedDate() {
        String dateString = dateFormat.format(calendar.getTime()); // get date
        dateHolder.setText(dateString); // display selected date
    }

    private void loadBookings() {
        // get date
        String date = dateFormat.format(calendar.getTime());
        // get bookings list from DB
        List<Booking> bookings = db.getBookingsForDate(date);

        BookingAdapter adapter = new BookingAdapter(bookings, true, booking -> {
            db.deleteBooking(booking); // delete booking

            // notify user upon cancelled booking
            NotificationHelper.sendNotification(
                    requireContext(),
                    "Booking Cancelled",
                    "The booking for the " + booking.date + " has been cancelled."
            );

            loadBookings(); // update list
        });

        recyclerView.setAdapter(adapter);
    }
}