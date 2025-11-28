package com.example.comp2000.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */

// CURRENT logged in user information
public class LoggedInUser {

    private String userId;
    private String displayName;
    // add bool for staff auth, 0/false = client, 1/true = staff
    private boolean isStaff;


    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isStaff() { return isStaff; }

    public void setStaff(boolean staff) { isStaff = staff; }
}