package com.example.fitnesstracker.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class PedometerActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

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

    private int steps;
}
