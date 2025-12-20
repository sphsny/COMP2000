package com.example.comp2000.ui.booking;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comp2000.R;
import com.example.comp2000.user.Roles;

// fragment that controls what to display on Booking screen
public class BookingRouterFragment extends Fragment {

    // route once on creation
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavController nav = NavHostFragment.findNavController(this);

        // change navigation to either client or staff booking screen based on roles, as the icon stays the same
        if (Roles.isStaff(requireContext())) {
            nav.navigate(R.id.navigation_staff_booking);
        } else {
            nav.navigate(R.id.navigation_client_booking);
        }
    }
}
