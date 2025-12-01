package com.example.comp2000.data.model;

public class MenuItem {
    public String name;
    public String details;
    public String price;
    public String imageUri; // item picture

    // constructor
    public MenuItem(String name, String details, String price, String imageUri) {
        this.name = name;
        this.details = details;
        this.price = price;
        this.imageUri = imageUri;
    }
}