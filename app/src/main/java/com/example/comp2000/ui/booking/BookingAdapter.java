package com.example.comp2000.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp2000.R;
import com.example.comp2000.data.model.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // toggle between user types
    private static final int TYPE_CLIENT = 0;
    private static final int TYPE_STAFF = 1;

    private final List<Booking> bookings; // list to hold bookings
    private final boolean isStaff; // get current users role, from the Staff and Client Booking Fragments

    // display bookings based on role
    public BookingAdapter(List<Booking> bookings, boolean isStaff) {
        this.bookings = bookings;
        this.isStaff = isStaff;
    }

    @Override
    public int getItemViewType(int position) {
        // get current users type, if true staff, if not client
        return isStaff ? TYPE_STAFF : TYPE_CLIENT;
    }

    @NonNull
    @Override
    // layout for recyclerview
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        // switch xml layout based on role
        if (viewType == TYPE_STAFF) {
            // staff layout
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_reservation, parent, false);
            return new StaffViewHolder(view);
        } else {
            // client layout
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_booking, parent, false);
            return new ClientViewHolder(view);
        }
    }

    // data that is displayed at specified position in recyyclerview while scrolling
    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder, int position) {

        Booking booking = bookings.get(position);

        // area that gets updated
        if (holder instanceof StaffViewHolder) {
            // staff side view holder
            StaffViewHolder h = (StaffViewHolder) holder;
            // pass values from booking model
            h.name.setText(booking.name);
            h.time.setText(booking.time);
            h.people.setText(booking.people + " people");
        } else {
            // client side view holder
            ClientViewHolder h = (ClientViewHolder) holder;
            // no name as client doesn't need to see their own name
            h.time.setText(booking.time);
            h.people.setText(booking.people + " people");
        }
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

    // client side view holder, elements shown in each recyclerview
    static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView time, people; // booking time (date, time, and amount of people)

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            // get elements from xml file
            time = itemView.findViewById(R.id.booking_time);
            people = itemView.findViewById(R.id.booking_people);
        }
    }
}
