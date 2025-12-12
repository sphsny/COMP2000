package com.example.comp2000.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

// SQLiteOpenHelper -> provides methods for executing SQL operations
public class RestaurantDB extends SQLiteOpenHelper {

    // MENU
    public static final String MENU = "menu";
    public static final String MENU_ID = "id";
    public static final String MENU_NAME = "name";
    public static final String MENU_DETAILS = "details";
    public static final String MENU_PRICE = "price";
    public static final String MENU_IMAGE = "imageName";


    // BOOKINGS
    public static final String BOOKINGS = "bookings";
    public static final String BOOKING_ID = "id";
    public static final String BOOKING_NAME = "name";
    public static final String BOOKING_DATE = "date";
    public static final String BOOKING_TIME = "time";
    public static final String BOOKING_PEOPLE = "people";

    // connect DB
    public RestaurantDB(@Nullable Context context) {
        super(context, "restaurant.db", null, 3);
    }

    // executed first time upon creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create table for Menu
        String createMenuTable = "CREATE TABLE " + MENU + " (" + MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MENU_NAME + " TEXT, " + MENU_DETAILS + " TEXT, " + MENU_PRICE + " TEXT, " + MENU_IMAGE + " TEXT)";

        // SQL query to create table for Bookings
        String createBookingsTable = "CREATE TABLE " + BOOKINGS + " (" + BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BOOKING_NAME + " TEXT, " + BOOKING_DATE + " TEXT, " + BOOKING_TIME + " TEXT, " + BOOKING_PEOPLE + " TEXT)";

        // execSQL(String sql) -> executes raw query
        db.execSQL(createMenuTable);
        db.execSQL(createBookingsTable);
    }

    // change DB version, recreate tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MENU);
        db.execSQL("DROP TABLE IF EXISTS " + BOOKINGS);
        onCreate(db);
    }

    // CRUD for Menu
    // add menu item (create)
    public long addMenuItem(MenuItem item) {
        SQLiteDatabase db = this.getWritableDatabase(); // open DB in writable mode
        ContentValues cv = new ContentValues(); // key value pairs

        cv.put(MENU_NAME, item.name);
        cv.put(MENU_DETAILS, item.details);
        cv.put(MENU_PRICE, item.price);
        cv.put(MENU_IMAGE, item.imageName);

        // insert into menu table
        return db.insert(MENU, null, cv);
    }

    // get menu items (read)
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // open DB in read only mode

        Cursor cursor = db.rawQuery("SELECT * FROM " + MENU, null); // read all menu table items

        // go through all menu items
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MENU_ID)); // assign each menu item ID
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MENU_NAME));
                String details = cursor.getString(cursor.getColumnIndexOrThrow(MENU_DETAILS));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(MENU_PRICE));
                String imageName = cursor.getString(cursor.getColumnIndexOrThrow(MENU_IMAGE));

                list.add(new MenuItem(id, name, details, price, imageName));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list; // return list of menu items
    }

    // update existing item upon changes (update)
    public void updateMenuItem(int id, MenuItem item) {
        SQLiteDatabase db = this.getWritableDatabase(); // open DB in writable mode
        ContentValues cv = new ContentValues();

        cv.put(MENU_NAME, item.name);
        cv.put(MENU_DETAILS, item.details);
        cv.put(MENU_PRICE, item.price);
        cv.put(MENU_IMAGE, item.imageName);

        db.update(MENU, cv, MENU_ID + "=?", new String[]{String.valueOf(id)});// SQLite/android studio db.update function
    }

    // delete existing menu item (delete)
    public void deleteMenuItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase(); // open DB in writable mode
        db.delete(MENU, MENU_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    // add booking function for client side (create)
    public boolean addBooking(Booking booking){
        SQLiteDatabase db = this.getWritableDatabase(); // opens database in write mode to insert, update or delete operations
        ContentValues cv = new ContentValues(); // key-value pair dictionary, each key is a column name, each value is the data to store

        // attributes that each booking contains: name, date, time and amount of people
        cv.put(BOOKING_NAME, booking.name);
        cv.put(BOOKING_DATE, booking.date);
        cv.put(BOOKING_TIME, booking.time);
        cv.put(BOOKING_PEOPLE, booking.people);

        long result = db.insert(BOOKINGS, null, cv);
        return result != -1;
    }

    // see bookings for specific date function for staff side
    public List<Booking> getBookingsForDate(String date) {
        List<Booking> outputList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + BOOKINGS + " WHERE " + BOOKING_DATE + "=? ORDER BY " + BOOKING_TIME + " ASC";

        // cursor: represents query results and allows row-by-row navigation for e.g. RecyclerView
        Cursor cursor = db.rawQuery(query, new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(BOOKING_NAME));
                String date_ = cursor.getString(cursor.getColumnIndexOrThrow(BOOKING_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(BOOKING_TIME));
                int people = cursor.getInt(cursor.getColumnIndexOrThrow(BOOKING_PEOPLE));

                Booking booking = new Booking(name, date_, time, people);
                outputList.add(booking);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return outputList;
    }

    // get bookings based on username for client side
    public List<Booking> getBookingsForUser(String username) {

        List<Booking> outputList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // open DB to read only

        String query = "SELECT * FROM " + BOOKINGS + " WHERE " + BOOKING_NAME + "=? ORDER BY " + BOOKING_DATE + " ASC, " + BOOKING_TIME + " ASC";
        // pass in SQL query with username parameter
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(BOOKING_NAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(BOOKING_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(BOOKING_TIME));
                int people = cursor.getInt(cursor.getColumnIndexOrThrow(BOOKING_PEOPLE));

                outputList.add(new Booking(name, date, time, people));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return outputList;
    }

    // delete booking (delete)
    public void deleteBooking(Booking booking){
        SQLiteDatabase db = this.getWritableDatabase(); // open DB editor
        db.delete(
                BOOKINGS, BOOKING_NAME + "=? AND " + BOOKING_DATE + "=? AND " + BOOKING_TIME + "=?",
                new String[]{booking.name, booking.date, booking.time}
        );
    }
}

// references:
// https://www.geeksforgeeks.org/android/how-to-delete-data-in-sqlite-database-in-android/
// https://github.com/Viveksbawa/COMP2000/tree/main/SQLite/app/src/main/java/com/example/sqllite