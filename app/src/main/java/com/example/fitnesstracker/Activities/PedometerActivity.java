package com.example.fitnesstracker.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

/**
 * The PedometerActivity class represents an activity that tracks and displays the number of steps taken by the user.
 */
public class PedometerActivity extends AppCompatActivity
{
    /**
     * Firebase authentication instance.
     */
    private FirebaseAuth firebaseAuth;
    /**
     * Currently logged-in Firebase user.
     */
    private FirebaseUser user;
    /**
     * Number of steps taken.
     */

    private int steps;
    /**
     * TextView to display the step count.
     */
    private TextView stepsCounter;
    /**
     * Circular progress bar to visualize step progress.
     */
    private CircularProgressBar progressCircular;
    /**
     * Countdown timer to periodically save the step count.
     */
    public static CountDownTimer countDownTimer = null;
    /**
     * Countdown timer to periodically update the interface.
     */
    public static CountDownTimer updateProgressThread = null;

    /**
     * Called when the activity is created.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        stepsCounter = findViewById(R.id.steps_counter);
        progressCircular = findViewById(R.id.circularProgressBar);

        Log.d("PedometerActivity", "Create activity");

        if(MapActivity.getInstance() == null)
        {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }

        resetSteps();
        loadSteps();
        updateInterface();

        if(countDownTimer == null)
        {
            countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000)
            {
                public void onTick(long millisUntilFinished)
                {
                    steps = MapActivity.steps;
                    saveSteps();
                }
                public void onFinish() {}
            }.start();
        }

        updateProgressThread = new CountDownTimer(Long.MAX_VALUE, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                updateInterface();
            }
            public void onFinish() {}
        }.start();
    }

    /**
     * Called when the activity is paused.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        saveSteps();
        updateProgressThread.cancel();
    }

    /**
     * Called when the activity is resumed.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        loadSteps();
    }

    /**
     * Resets the step count and sets up a long tap listener to reset the steps.
     */
    private void resetSteps()
    {
        TextView stepsText = findViewById(R.id.steps_text);
        stepsText.setOnClickListener(unused -> Toast.makeText(this, "Long tap to reset!", Toast.LENGTH_SHORT).show());
        stepsText.setOnLongClickListener(unused ->
        {
            MapActivity.steps = 0;
            steps = 0;
            TextView stepsCounter = findViewById(R.id.steps_counter);
            stepsCounter.setText(String.valueOf(steps));
            saveSteps();

            return true;
        });
    }

    /**
     * Loads the step count from shared preferences.
     */
    private void loadSteps()
    {
        if (user != null)
        {
            SharedPreferences prefs = getSharedPreferences(user.getUid(), Context.MODE_PRIVATE);
            steps = prefs.getInt("steps", 0);
            MapActivity.steps = steps;
        }
    }

    /**
     * Saves the step count to shared preferences.
     */
    private void saveSteps()
    {
        if (user != null)
        {
            SharedPreferences prefs = getSharedPreferences(user.getUid(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("steps", MapActivity.steps);
            editor.apply();
        }
    }

    /**
     * Updates the user interface with the current step count and progress.
     */
    private void updateInterface()
    {
        stepsCounter.setText(String.valueOf(MapActivity.steps));
        progressCircular.setProgressMax(ChallengesActivity.getLastIncompletedMilestone());
        progressCircular.setProgressWithAnimation(MapActivity.steps);
    }
}
