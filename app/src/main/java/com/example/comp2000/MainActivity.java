package com.example.comp2000;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.comp2000.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottom_nav);

        androidx.navigation.fragment.NavHostFragment navHostFragment =
                (androidx.navigation.fragment.NavHostFragment)
                        getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(navView, navController);


            ImageView notificationIcon = findViewById(R.id.notification_icon);

            // not correctly working!!
            notificationIcon.setOnClickListener(v -> {
                if (navController.getCurrentDestination() == null ||
                        navController.getCurrentDestination().getId() != R.id.navigation_notifications) {
                    navController.navigate(R.id.navigation_notifications);
                }
            });

        } else {
            throw new IllegalStateException("NavHostFragment not found in activity_main.xml");
        }
    }
}