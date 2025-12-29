package com.example.comp2000.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp2000.R;
import com.example.comp2000.models.Booking;

import java.util.List;

public class BookingStaffAdapter extends RecyclerView.Adapter<BookingStaffAdapter.StaffViewHolder> {

    private final List<Booking> bookings; // list to hold bookings

    private final OnCancelClickListener cancelListener; // listener for cancel button (staff side)

    public interface OnCancelClickListener {
        void onCancel(Booking booking);
    }

    // booking constructor (staff side)
    public BookingStaffAdapter(List<Booking> bookings, OnCancelClickListener cancelListener) {
        this.bookings = bookings;
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    // layout for recyclerview (staff)
    public StaffViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        // staff layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation, parent, false);

        return new StaffViewHolder(view);
    }

    // data that is displayed at specified position in recyclerview while scrolling
    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder h, int position) {

        Booking booking = bookings.get(position);

        // pass values from booking model
        h.name.setText(booking.name);
        h.time.setText(booking.time);
        h.people.setText(booking.people + " people");

        // set cancel button
        h.cancel.setOnClickListener(v -> {
            if (cancelListener != null)
                cancelListener.onCancel(booking);
        });
    }

    // helper for recyclerview to know how many items to display in total
    @Override
    public int getItemCount() {
        return bookings.size();
    }

    // staff side view holder, elements shown in each recyclerview
    static class StaffViewHolder extends RecyclerView.ViewHolder {
        TextView name, time, people; // user and booking info
        Button cancel; // cancel booking

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            // get elements from xml file
            name = itemView.findViewById(R.id.res_name);
            time = itemView.findViewById(R.id.res_time);
            people = itemView.findViewById(R.id.res_people);
            cancel = itemView.findViewById(R.id.res_cancel);
        }
    }
}
