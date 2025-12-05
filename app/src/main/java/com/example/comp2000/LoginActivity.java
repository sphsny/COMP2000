package com.example.comp2000;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comp2000.data.model.User;
import com.google.gson.Gson;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;

    // API connection parameters
    private static final String BASE_URL = "http://10.240.72.69/comp2000/coursework/";
    private static final String STUDENT_ID = "student_SK"; // custom created DB

    // link gson
    private final Gson gson = new Gson();

    // create layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        Button login = findViewById(R.id.login);

        // run login function when button is clicked
        login.setOnClickListener(v -> Login());
    }

    // login function
    private void Login() {
        // get user input from text fields
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();

        String url = BASE_URL + "read_user/" + STUDENT_ID + "/" + username;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {

                    // get username
                    JSONObject userJson = response.optJSONObject("user");

                    // run if username is not found
                    if (userJson == null) {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // convert json into user model using gson
                    User user = gson.fromJson(userJson.toString(), User.class);

                    // compare password values
                    if (!password.equals(user.password)) {
                        Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // set staff role
                    boolean isStaff = user.usertype != null && user.usertype.equalsIgnoreCase("staff");
                    Roles.setStaff(this, isStaff);

                    // launch MainActivity via intent
                    Intent intent = new Intent(this, MainActivity.class);
                    // pass user parameters
                    intent.putExtra("role", user.usertype);
                    intent.putExtra("username", user.username);

                    startActivity(intent);
                    finish();
                },
                // catch volley errors
                error -> {
                    Toast.makeText(this, "Volley error: " + error.toString(), Toast.LENGTH_LONG).show();
                }
        );
        // add request to volley queue
        Volley.newRequestQueue(this).add(request);
    }
}
