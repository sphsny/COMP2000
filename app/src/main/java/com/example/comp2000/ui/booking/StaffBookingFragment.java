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

public class StaffBookingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        // define view to inflate
        View view = inflater.inflate(R.layout.fragment_reservations, container, false);

        // get recyclerview from fragment_reservations.xml
        RecyclerView recyclerView = view.findViewById(R.id.reservationsRecyclerView);
        // view recyclerview as vertical scrolling list
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // sample data
        List<Booking> reservations = new ArrayList<>(); // create list holding samples
        reservations.add(new Booking("John Doe", "01/12/2025", "18:00", 3));
        reservations.add(new Booking("Mary Smith", "01/12/2025", "19:30", 5));

        BookingAdapter adapter = new BookingAdapter(reservations, true);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
