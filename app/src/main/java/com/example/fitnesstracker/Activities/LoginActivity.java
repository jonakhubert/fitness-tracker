package com.example.fitnesstracker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstracker.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * Represents the LoginActivity class, responsible for handling user login.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * EditText for entering email and password
     */
    private EditText email, password;
    /**
     * Button for initiating login
     */
    private Button loginButton;
    /**
     * TextView for navigating to the registration activity and initiating password reset
     */
    private TextView registerRef, forgotPassword;
    /**
     * ProgressBar for indicating login progress
     */
    private ProgressBar progressBar;
    /**
     * Firebase authentication instance
     */
    private FirebaseAuth firebaseAuth;

    /**
     * Called when the activity is created.
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerRef = findViewById(R.id.registerRef);
        forgotPassword = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.loginProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> {
            String emailStr = email.getText().toString().trim();
            String passwordStr = password.getText().toString().trim();

            if(TextUtils.isEmpty(emailStr))
            {
                email.setError("Email is required.");
                return;
            }

            if(TextUtils.isEmpty(passwordStr)) {
                password.setError("Password is required.");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                    updateStepsForUser();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });

        registerRef.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));

        forgotPassword.setOnClickListener(v -> {
            EditText resetEmail = new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset password?");
            passwordResetDialog.setMessage("Enter your email to receive reset link.");
            passwordResetDialog.setView(resetEmail);

            passwordResetDialog.setNegativeButton("Cancel", (dialog, which) -> {});

            passwordResetDialog.setPositiveButton("Send", (dialog, which) -> {
                String email = resetEmail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(unused -> Toast.makeText(LoginActivity.this, "Reset link has been sent.", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error! Reset link has not been sent. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });

            passwordResetDialog.create().show();
        });
    }

    /**
     * Update the number of steps for the current user from shared preferences.
     */
    private void updateStepsForUser()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null)
        {
            SharedPreferences prefs = getSharedPreferences(user.getUid(), Context.MODE_PRIVATE);
            MapActivity.steps = prefs.getInt("steps", 0);
        }
    }
}