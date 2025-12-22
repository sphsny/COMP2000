package com.example.comp2000.notifications;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationSettings {

    // preference from local storage either true or false
    private static final String PREFERENCE = "notification_prefs";

    // keys for key-value map, retrieving the user preference for notifications (true/false) from SharedPreferences
    private static final String UPDATED_BOOKING = "notify_booking_update";
    private static final String CANCELLED_BOOKING = "notify_booking_cancel";

    // helper to get prefs from local storage
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }

    // notifications on new/updated booking

    // read preference whether switch is enabled
    public static boolean bookingUpdateEnabled(Context context) {
        return getPrefs(context).getBoolean(UPDATED_BOOKING, true);
    }

    // save user preference upon switch toggle
    public static void setBookingUpdateEnabled(Context context, boolean enabled) {
        getPrefs(context)
                .edit()
                .putBoolean(UPDATED_BOOKING, enabled)
                .apply();
    }

    // notifications on cancelled booking
    public static boolean bookingCancelledEnabled(Context context) {
        return getPrefs(context).getBoolean(CANCELLED_BOOKING, true);
    }

    public static void setBookingCancelledEnabled(Context context, boolean enabled) {
        getPrefs(context)
                .edit()
                .putBoolean(CANCELLED_BOOKING, enabled)
                .apply();
    }
}
