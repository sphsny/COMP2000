package com.example.comp2000.ui.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp2000.R;
import com.example.comp2000.data.model.Booking;

import java.util.ArrayList;
import java.util.List;

public class ClientBookingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        // get recyclerview from fragment_booking.xml ! rename either this or the staff recyclerview so they are named in the same style
        RecyclerView recyclerView = view.findViewById(R.id.reservation_list);
        // convert to vertical scrolling list
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // sample data
        List<Booking> clientBookings = new ArrayList<>();
        clientBookings.add(new Booking("John Doe", "01/12/2025", "19:00", 2));
        clientBookings.add(new Booking("Mary Smith", "03/12/2025", "20:00", 4));

        BookingAdapter adapter = new BookingAdapter(clientBookings, false);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
