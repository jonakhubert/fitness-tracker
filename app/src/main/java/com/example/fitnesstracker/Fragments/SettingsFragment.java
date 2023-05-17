package com.example.fitnesstracker.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstracker.Activities.LoginActivity;
import com.example.fitnesstracker.Activities.PedometerActivity;
import com.example.fitnesstracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingsFragment extends Fragment {
    private View view;
    private TextView verifyMessage;
    private Button verifyButton;
    private Button logoutButton;
    private FirebaseAuth firebaseAuth;

    private Switch switchMode;
    private static boolean nightMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        verifyMessage = view.findViewById(R.id.verifyMessage);
        verifyButton = view.findViewById(R.id.verifyButton);
        firebaseAuth = FirebaseAuth.getInstance();
        logoutButton = view.findViewById(R.id.logout);
        switchMode = view.findViewById(R.id.switch_mode);

        sharedPreferences = requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

        if(nightMode)
        {
            switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switchMode.setOnClickListener(v -> {
            if(nightMode)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor = sharedPreferences.edit();
                editor.putBoolean("night", false);
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = sharedPreferences.edit();
                editor.putBoolean("night", true);
            }
            editor.apply();
        });

        logoutButton.setOnClickListener(v -> logout());

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null && !user.isEmailVerified())
        {
            verifyMessage.setVisibility(View.VISIBLE);
            verifyButton.setVisibility(View.VISIBLE);

            verifyButton.setOnClickListener(v -> user.sendEmailVerification()
                    .addOnSuccessListener(unused -> Toast.makeText(v.getContext(), "Verification email has been sent.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Verification email has not been sent. " + e.getMessage(), Toast.LENGTH_SHORT).show()));
        }

        return view;
    }

    public void logout() {
        if(PedometerActivity.countDownTimer != null)
        {
            PedometerActivity.countDownTimer.cancel();
            PedometerActivity.countDownTimer = null;
        }

        firebaseAuth.signOut();
        startActivity(new Intent(requireActivity().getApplicationContext(), LoginActivity.class));
        requireActivity().finish();
    }
}