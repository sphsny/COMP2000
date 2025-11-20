package com.example.comp2000.data.model;

// data model used for bookings by both client and staff side
public class Booking {
    public String date;
    public String time;
    public int people;

    public Booking(String date, String time, int people) {
        this.date = date;
        this.time = time;
        this.people = people;
    }
}
