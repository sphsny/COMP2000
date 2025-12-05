package com.example.comp2000.data.model;

// matches the User data returned by the API, so that GSON can convert the JSON object into a Java object
public class User {
    public String username;
    public String password;
    public String firstname;
    public String lastname;
    public String email;
    public String contact;
    public String usertype;
}