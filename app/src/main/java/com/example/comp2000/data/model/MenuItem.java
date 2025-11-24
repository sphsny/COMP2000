package com.example.comp2000.data.model;

public class MenuItem {
    public String name;
    public String price;
    public int image;

    public MenuItem(String name, String price, int image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }
}

// use as
// MenuItem item = new MenuItem("Pizza", "£10", R.drawable.pizza);