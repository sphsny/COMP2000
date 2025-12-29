package com.example.comp2000.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comp2000.notifications.NotificationSettings;
import com.example.comp2000.user.LoginActivity;
import com.example.comp2000.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ProfileFragment extends Fragment {

    // text views
    private TextView usernameView, firstnameView, lastnameView, emailView, contactView;

    // create view hierarchy
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    // create layout
    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // get UI elements
        usernameView = view.findViewById(R.id.username);
        firstnameView = view.findViewById(R.id.firstname);
        lastnameView = view.findViewById(R.id.lastname);
        emailView = view.findViewById(R.id.email);
        contactView = view.findViewById(R.id.contact);
        Button logOutButton = view.findViewById(R.id.log_out);

        // get user data from local storage
        loadLocalProfile();

        // link switches for notification toggle
        SwitchMaterial bookingUpdateSwitch = view.findViewById(R.id.notification_switch1);
        SwitchMaterial bookingCancelSwitch = view.findViewById(R.id.notification_switch2);

        // load stored preferences from local storage
        bookingUpdateSwitch.setChecked(
                NotificationSettings.bookingUpdateEnabled(requireContext())
        );

        bookingCancelSwitch.setChecked(
                NotificationSettings.bookingCancelledEnabled(requireContext())
        );

        // save preferences on change
        bookingUpdateSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                NotificationSettings.setBookingUpdateEnabled(
                        requireContext(),
                        isChecked
                )
        );

        bookingCancelSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                NotificationSettings.setBookingCancelledEnabled(
                        requireContext(),
                        isChecked
                )
        );

        // logout logic on button click
        logOutButton.setOnClickListener(v -> {
            // get context state and clear its values
            requireContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().apply();
            // switch to LoginActivity
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });
    }

    // load stored user details from shared preferences
    private void loadLocalProfile() {
        var localStorage = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        usernameView.setText("Username: " + localStorage.getString("username", ""));
        firstnameView.setText("First name: " + localStorage.getString("firstname", ""));
        lastnameView.setText("Last name: " + localStorage.getString("lastname", ""));
        emailView.setText("Email: " + localStorage.getString("email", ""));
        contactView.setText("Contact: " + localStorage.getString("contact", ""));
    }
}
