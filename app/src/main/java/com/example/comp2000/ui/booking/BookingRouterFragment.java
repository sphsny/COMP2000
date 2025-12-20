package com.example.comp2000.ui.booking;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comp2000.R;
import com.example.comp2000.user.Roles;

// fragment that controls what to display on Booking screen
public class BookingRouterFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume(); // called when fragment is visible to user and actively running

        // get navcontroller ! could this be simplified in main/navcontroller?
        NavController nav = NavHostFragment.findNavController(this);

        // change navigation to either client or staff booking screen based on roles, as the icon stays the same
        if (Roles.isStaff(requireContext())) {
            nav.navigate(R.id.navigation_staff_booking);
        } else {
            nav.navigate(R.id.navigation_client_booking);
        }
    }
}
