package com.example.comp2000.models;

// data model used for bookings by both client and staff side
public class Booking {
    public int id; // to retrieve unique booking
    public String username; // name e.g. John Doe - from loggedInUser, defined in Adapter, passed when making a booking
    public String name;
    public String date; // booking date e.g. 14/10/2025
    public String time; // booking time e.g. 14:00
    public int people; // amount of people e.g. 2

    // used for existing bookings
    public Booking(int id, String username, String name, String date, String time, int people) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.date = date;
        this.time = time;
        this.people = people;
    }

    // used for new bookings
    public Booking(String username, String name, String date, String time, int people) {
        this.username = username;
        this.name = name;
        this.date = date;
        this.time = time;
        this.people = people;
    }
}
