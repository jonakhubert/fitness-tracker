package com.example.fitnesstracker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The RegisterActivity class represents an activity that allows users to register a new account.
 */
public class RegisterActivity extends AppCompatActivity
{
    /**
     * EditText field for entering the user's name, email and password.
     */
    private EditText name, email, password;
    /**
     * Button for registering the user.
     */
    private Button registerButton;
    /**
     * TextView for navigating to the login activity.
     */
    private TextView loginRef;
    /**
     * ProgressBar for indicating the registration progress.
     */
    private ProgressBar progressBar;
    /**
     * Firebase authentication instance.
     */
    private FirebaseAuth firebaseAuth;
    /**
     * Firebase Firestore instance.
     */
    private FirebaseFirestore firebaseFirestore;

    /**
     * Called when the activity is created.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        registerButton = findViewById(R.id.registerButton);
        loginRef = findViewById(R.id.loginRef);
        progressBar = findViewById(R.id.registerProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        registerButton.setOnClickListener(v -> {
            String emailStr = email.getText().toString().trim();
            String passwordStr = password.getText().toString().trim();

            if(TextUtils.isEmpty(emailStr))
            {
                email.setError("Email is required.");
                return;
            }

            if(TextUtils.isEmpty(passwordStr))
            {
                password.setError("Password is required.");
                return;
            }

            if(password.length() < 6)
            {
                password.setError("Password must contain more than 6 characters.");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(unused -> Toast.makeText(RegisterActivity.this, "Verification email has been sent.", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Verification email has not been sent. + " + e.getMessage(), Toast.LENGTH_SHORT).show());

                    DocumentReference documentReference = firebaseFirestore.collection("users").document(user.getUid());
                    Map<String, Object> fUser = new HashMap<>();
                    fUser.put("name", name.getText().toString());
                    fUser.put("email", email.getText().toString());

                    documentReference.set(fUser);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });

        loginRef.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
    }
}