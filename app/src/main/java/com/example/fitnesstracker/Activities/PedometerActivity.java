package com.example.fitnesstracker.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Objects;

public class PedometerActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        loadSteps();
        Log.d("PedometerActivity", "Create activity");

        if(MapActivity.getInstance() == null)
        {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }

        resetSteps();

        new CountDownTimer(Long.MAX_VALUE, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                steps = MapActivity.steps;

                TextView stepsCounter = findViewById(R.id.steps_counter);
                stepsCounter.setText(String.valueOf(steps));

                CircularProgressBar progressCircular = findViewById(R.id.circularProgressBar);
                progressCircular.setProgressWithAnimation(steps);
            }
            public void onFinish() {}
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSteps();
    }

    private void resetSteps()
    {
        TextView stepsText = findViewById(R.id.steps_text);
        stepsText.setOnClickListener(unused -> Toast.makeText(this, "Long tap to reset!", Toast.LENGTH_SHORT).show());
        stepsText.setOnLongClickListener(unused ->
        {
            MapActivity.steps = 0;
            steps = 0;
            TextView stepsCounter = findViewById(R.id.steps_counter);
            stepsCounter.setText(0);

            return true;
        });
    }

    private void loadSteps()
    {
        SharedPreferences prefs = getSharedPreferences(user.getUid(), Context.MODE_PRIVATE);
        steps = prefs.getInt("steps", 0);
        MapActivity.steps = steps;
    }

    private void saveSteps()
    {
        if (firebaseAuth.getCurrentUser() != null) {
            SharedPreferences prefs = getSharedPreferences(firebaseAuth.getCurrentUser().getUid(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("steps", MapActivity.steps);
            editor.apply();
        }
    }

    private int steps;
}
