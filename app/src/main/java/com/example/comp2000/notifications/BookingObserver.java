package com.example.comp2000.notifications;

// observer: gets triggered when subject state changes
public interface BookingObserver {
    void onBookingUpdated(); // on new/updated booking
    void onBookingCancelled(); // on cancelled booking
}
