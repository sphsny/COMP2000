package com.example.comp2000.ui.booking;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp2000.notifications.BookingManager;
import com.example.comp2000.notifications.NotificationHelper;
import com.example.comp2000.R;
import com.example.comp2000.models.Booking;
import com.example.comp2000.models.RestaurantDB;
import com.example.comp2000.user.Roles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientBookingFragment extends Fragment {

    // SQLite DB
    private RestaurantDB db;

    // XML layout
    private EditText dateInput;
    private AutoCompleteTextView peopleInput;
    private AutoCompleteTextView timeInput;

    // set up recyclerview
    private RecyclerView recyclerView;
    // notifications observer
    private BookingManager bookingManager;
    private NotificationHelper notificationHelper;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_client_booking, container, false);

        // add notifications observer
        bookingManager = new BookingManager();
        notificationHelper = new NotificationHelper(requireContext());

        bookingManager.addObserver(notificationHelper);

        // connect DB
        db = new RestaurantDB(requireContext());

        // connect XML layout
        dateInput = view.findViewById(R.id.date);
        peopleInput = view.findViewById(R.id.people);
        timeInput = view.findViewById(R.id.time);
        Button submitButton = view.findViewById(R.id.submit_button);

        // set up recycler view
        recyclerView = view.findViewById(R.id.reservation_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // fetch list of bookings
        loadClientBookings();

        // select date in calendar via DatePickerDialog
        dateInput.setOnClickListener(v -> {
            // select date via dialog
            Calendar cal = Calendar.getInstance();
            int year  = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day   = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog selectDate = new DatePickerDialog(requireContext(),
                    (picker, y, m, d) -> {
                        String selected = d + "/" + (m + 1) + "/" + y; // format date in input field
                        dateInput.setText(selected); // set selected date
                    },
                    year, month, day // start from today's date
            );

            selectDate.show(); // open dialog
        });

        // drop down to select amount of people
        peopleInput.setOnClickListener(v -> {
            // drop down menu options: select amount from 1 to 9 people
            String[] peopleOptions = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
            // create drop down
            ListPopupWindow popup = new ListPopupWindow(requireContext());
            // connect to array
            popup.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, peopleOptions));
            // set anchor view
            popup.setAnchorView(peopleInput);
            // listen when list item is clicked
            popup.setOnItemClickListener((parent, row, position, id) -> {
                peopleInput.setText(peopleOptions[position]); // pass value of clicked row
                popup.dismiss(); // close dropdown menu
            });
            popup.show(); // display the dropdown menu
        });

        // drop down to select a time slot
        timeInput.setOnClickListener(v -> {
            // drop down menu options: sample times, e.g. assume restaurant accepts reservations from 2 to 8 pm
            String[] times = {"14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00"};
            // create drop down
            ListPopupWindow popup = new ListPopupWindow(requireContext());
            // connect to array
            popup.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, times));
            // set anchor view
            popup.setAnchorView(timeInput);
            // listen when list item is clicked
            popup.setOnItemClickListener((parent, row, position, id) -> {
                timeInput.setText(times[position]);
                popup.dismiss();
            });
            popup.show();
        });

        // submit button, function to save values to SQLite DB
        submitButton.setOnClickListener(v -> submitBooking());

        return view;
    }

    private void submitBooking() {
        // pass in values
        String username = Roles.getUsername(requireContext());
        String date = dateInput.getText().toString().trim();
        String time = timeInput.getText().toString().trim();
        String peopleStr = peopleInput.getText().toString().trim();

        // prompt user to fill in ALL fields
        if (date.isEmpty() || time.isEmpty() || peopleStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in ALL fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int people = Integer.parseInt(peopleStr); // convert people int to string, may be not ideal for DB efficiency ...

        Booking booking = new Booking(username, date, time, people); // create new booking
        boolean success = db.addBooking(booking); // add to DB on success

        // provide feedback to user
        if (success) {
            // notify staff about new booking
            bookingManager.updateBooking();
            Toast.makeText(getContext(), "Booking created!", Toast.LENGTH_SHORT).show();
            loadClientBookings();
        } else {
            Toast.makeText(getContext(), "Failed to create booking.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadClientBookings() {
        // get global username from SharedPreferences
        String username = Roles.getUsername(requireContext());

        // display empty list if no associated bookings found
        List<Booking> clientBookings = (username == null) ? new ArrayList<>() : db.getBookingsForUser(username);

        // create recyclerview adapter
        BookingClientAdapter adapter =
                new BookingClientAdapter(clientBookings, booking -> {
                    db.deleteBooking(booking); // remove deleted booking from database
                    loadClientBookings(); // reload the booking list on the ui
                });

        // attach adapter to recyclerview to display bookings
        recyclerView.setAdapter(adapter);
    }
}

// references:
// https://developer.android.com/reference/android/icu/util/Calendar
// https://developer.android.com/reference/android/app/DatePickerDialog
// https://developer.android.com/reference/android/widget/ListPopupWindow#ListPopupWindow(android.content.Context)