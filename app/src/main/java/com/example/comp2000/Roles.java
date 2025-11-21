package com.example.comp2000;

import android.content.Context;

// helper class for RBAC where staff is true and client is false
public class Roles {
    // getter method, checks if current user is staff
    public static boolean isStaff(Context context) {
        // isStaff value from SharedPreferences (in-built key value storage, shared across whole app)
        return context.getSharedPreferences("user", Context.MODE_PRIVATE)
                .getBoolean("isStaff", false); // default value is false (non-staff user)
    }

    // setter method, saves the user role to sharedPreferences after login for the UI to display staff elements
    public static void setStaff(Context context, boolean isStaff) {
        context.getSharedPreferences("user", Context.MODE_PRIVATE)
                .edit() // in-built editor to overwrite the role
                .putBoolean("isStaff", isStaff) // value to store
                .apply(); // save changes asynchronously
    }
}

// example usage:
// if (Roles.isStaff(requireContext())) {
//        button.setVisibility(View.VISIBLE); // button visible for staff
//    } else {
//        button.setVisibility(View.GONE); // and hidden for client
//    }