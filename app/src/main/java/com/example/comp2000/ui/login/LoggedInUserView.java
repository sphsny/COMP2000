package com.example.comp2000.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private boolean isStaff;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, boolean staff) {

        this.displayName = displayName;
        this.isStaff = staff;
    }

    // getter
    String getDisplayName() {
        return displayName;
    }
    // setter
    public boolean isStaff() { return isStaff; }
}