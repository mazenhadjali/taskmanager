package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;

import com.example.taskmanager.fragments.AddTaskFragment;
import com.example.taskmanager.fragments.CalendarFragment;
import com.example.taskmanager.fragments.MainFragment;
import com.example.taskmanager.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Dashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    MainFragment mainFragment = new MainFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    AddTaskFragment addTaskFragment = new AddTaskFragment();
    CalendarFragment calendarFragment = new CalendarFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);

        String userName = "Hadj Ali Mazen";
        String greetingMessage = "Hello " + userName;

        toolbarTitle.setText(greetingMessage);

        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.notification) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.calendar) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, calendarFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.add) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, addTaskFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                    return true;
                }
                return false;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
    }


}
