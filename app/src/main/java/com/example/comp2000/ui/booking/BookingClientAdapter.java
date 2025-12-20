package com.example.comp2000.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp2000.R;
import com.example.comp2000.models.Booking;

import java.util.List;

public class BookingClientAdapter extends RecyclerView.Adapter<BookingClientAdapter.ClientViewHolder> {

    private final List<Booking> bookings; // list to hold bookings

    private final OnCancelClickListener cancelListener; // listener for delete button (client side)

    public interface OnCancelClickListener {
        void onCancel(Booking booking);
    }

    // booking constructor (client side)
    public BookingClientAdapter(List<Booking> bookings, OnCancelClickListener cancelListener) {
        this.bookings = bookings;
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    // layout for recyclerview (client)
    public ClientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        // client layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);

        return new ClientViewHolder(view);
    }

    // data that is displayed at specified position in recyclerview while scrolling
    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder h, int position) {

        Booking booking = bookings.get(position);

        // no name as client doesn't need to see their own name
        // date + time
        String dateTime = booking.date + " - " + booking.time;
        h.time.setText(dateTime);

        // amount of people
        h.people.setText(booking.people + " people");

        // delete booking
        h.delete.setOnClickListener(v -> {
            if (cancelListener != null)
                cancelListener.onCancel(booking);
        });

        // edit booking (not implemented yet)
    }

    // helper for recyclerview to know how many items to display in total
    @Override
    public int getItemCount() {
        return bookings.size();
    }

    // client side view holder, elements shown in each recyclerview
    static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView time, people; // booking time (date, time, and amount of people)
        ImageButton edit, delete;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            // get elements from xml file
            time = itemView.findViewById(R.id.booking_time);
            people = itemView.findViewById(R.id.booking_people);
            edit = itemView.findViewById(R.id.res_edit);
            delete = itemView.findViewById(R.id.res_delete);
        }
    }
}
