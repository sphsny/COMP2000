package com.example.comp2000.notifications;

import java.util.ArrayList;
import java.util.List;

// observable subject
public class BookingManager {
    private final List<BookingObserver> observers = new ArrayList<>();

    // connect observer
    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    // events to listen to
    // new booking gets made/updated (client to admin)
    public void updateBooking() {
        for (BookingObserver observer : observers) {
            observer.onBookingUpdated();
        }
    }

    // booking gets cancelled (admin to client)
    public void cancelBooking() {
        for (BookingObserver observer : observers) {
            observer.onBookingCancelled();
        }
    }
}
