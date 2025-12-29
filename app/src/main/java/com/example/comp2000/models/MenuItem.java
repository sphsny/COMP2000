package com.example.comp2000.models;

public class MenuItem {
    public int id; // keep count of menu items in DB
    public String name;
    public String details;
    public String price;
    public String imageName; // string to image path

    // constructor with ID for fetching items from DB
    public MenuItem(int id, String name, String details, String price, String imageName) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.price = price;
        this.imageName = imageName;
    }

    // constructor without ID for creating a new item
    public MenuItem(String name, String details, String price, String imageName) {
        this(-1, name, details, price, imageName);
    }
}