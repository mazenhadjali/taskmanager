package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.core.DataBaseHelper;


public class Authentication extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    private DataBaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        databaseHelper = new DataBaseHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the username and password from EditText fields
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (checkUserCredentials(username, password)) {
                    // Authentication successful, you can proceed to the next activity or perform other actions
                    Toast.makeText(Authentication.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Authentication.this, Dashboard.class);
                    startActivity(i);
                    finish();
                } else {
                    // Authentication failed, show an error message
                    Toast.makeText(Authentication.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean checkUserCredentials(String username, String password) {
        return databaseHelper.isUserExists(username, password);
    }
}
