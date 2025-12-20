package com.example.comp2000;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // set role and user parameters
    public static String CURRENT_ROLE = "";
    public static String CURRENT_USER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        // make status bar icons containing time and icons white
        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(false);

        // retrieve username and user role from shared preferences (works offline)
        CURRENT_USER = Roles.getUsername(this);
        CURRENT_ROLE = Roles.isStaff(this) ? "staff" : "client";

        // permissions required for notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }

        // find bottom nav bar from bottom nav xml
        BottomNavigationView navView = findViewById(R.id.bottom_nav);

        // find nav host fragment from nav host fragment xml
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        // make sure nav host fragment is found
        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment not found in activity_main.xml");
        }

        // get nav controller from nav host fragment xml to navigate between screens
        NavController navController = navHostFragment.getNavController();
        // connect bottom nav bar to nav controller to navigate via buttons
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onResume() {
        // starts when activity starts user interaction
        super.onResume();
        // show staff indicator if user is staff
        setStaffBannerVisible(checkIfUserIsStaff());
    }

    // check user role from Roles.java
    private boolean checkIfUserIsStaff() {
        return Roles.isStaff(this);
    }


    // helper method for staff indicator in header
    public void setStaffBannerVisible(boolean visible) {
        // find element by id from header xml
        TextView staffIndicator = findViewById(R.id.staffIndicator);
        // show staff indicator when user is staff, otherwise not
        staffIndicator.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
