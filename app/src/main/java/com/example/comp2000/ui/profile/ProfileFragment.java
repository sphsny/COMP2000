package com.example.comp2000.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comp2000.user.LoginActivity;
import com.example.comp2000.MainActivity;
import com.example.comp2000.R;
import com.example.comp2000.models.User;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    // api connection parameters
    private static final String BASE_URL = "http://10.240.72.69/comp2000/coursework/";
    private static final String STUDENT_ID = "student_SK";

    // text views
    private TextView usernameView, firstnameView, lastnameView, emailView, contactView;

    // link gson
    private final Gson gson = new Gson();

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

        // load profile via API based on currently logged in user
        loadUserData(MainActivity.CURRENT_USER);

        // logout logic on button click
        logOutButton.setOnClickListener(v -> {
            // get context state and clear its values
            requireContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().apply();
            // switch to LoginActivity
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });
    }

    // load user data using Gson
    private void loadUserData(String username) {
        // api connection
        String url = BASE_URL + "read_user/" + STUDENT_ID + "/" + username;

        // object request for single user's data
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {

            if (!isAdded()) return;

            // extract json data from user object
            JSONObject userJson = response.optJSONObject("user");

            // catch error if no user data could be loaded
            if (userJson == null) {
                loadLocalProfile(); // load locally saved data instead
                return;
            }

            // convert json into user model using gson
            User user = gson.fromJson(userJson.toString(), User.class);

            saveUserToLocal(user); // save user data to local storage
            loadLocalProfile(); // load user data from local storage
        },
            // catch volley error
            error -> {
                if (!isAdded()) return; // safe volley error message https://developer.android.com/reference/androidx/fragment/app/Fragment#isAdded()
                Toast.makeText(requireContext(), "Could not update user data", Toast.LENGTH_SHORT).show();
                loadLocalProfile();
            }
        );
        // pass request into volley queue
        Volley.newRequestQueue(requireContext()).add(request);
    }

    // save user data from API to local storage (shared preferences)
    private void saveUserToLocal(User user) {
        var localStorage = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit();

        localStorage.putString("username", user.username);
        localStorage.putString("firstname", user.firstname);
        localStorage.putString("lastname", user.lastname);
        localStorage.putString("email", user.email);
        localStorage.putString("contact", user.contact);

        localStorage.apply();
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
