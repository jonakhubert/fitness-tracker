package com.example.fitnesstracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.fitnesstracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Represents the MainActivity class, responsible for managing the main activity of the application.
 */
public class MainActivity extends AppCompatActivity
{
    /**
     * Bottom navigation view for navigating between fragments
     */
    private BottomNavigationView bottomNavigationView;

    /**
     * Called when the activity is created.
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(findViewById(R.id.fragment));

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        SharedPreferences sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}