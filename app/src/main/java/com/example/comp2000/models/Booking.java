package com.example.comp2000.models;

// data model used for bookings by both client and staff side
public class Booking {
    public String name; // name e.g. John Doe - from loggedInUser, defined in Adapter, passed when making a booking
    public String date; // booking date e.g. 14/10/2025
    public String time; // booking time e.g. 14:00
    public int people; // amount of people e.g. 2

    public Booking(String name, String date, String time, int people) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.people = people;
    }
}
