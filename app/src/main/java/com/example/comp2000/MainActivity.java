package com.example.comp2000;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // find bottom nav bar from bottom nav xml
        BottomNavigationView navView = findViewById(R.id.bottom_nav);

        // find navhostfragment from navhostfragment xml
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        // make sure navhostfragment is found
        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment not found in activity_main.xml");
        }

        // get navcontroller from navhostfragment xml to navigate between screens
        NavController navController = navHostFragment.getNavController();
        // connect bottom nav bar to navcontroller to navigate via buttons
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
