package com.example.fitnesstracker.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class PedometerActivity extends AppCompatActivity implements SensorEventListener
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        loadData();
        resetSteps();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        running = true;
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if(stepSensor == null)
        {
            Toast.makeText(this, "No suitable sensor detected on the device!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            Log.d("\n\n\n\nSensorRegistration\n\n\n\n", "\n\n\n\nSensor successfully registered!\n\n\n\n");

        }
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        Toast.makeText(this, "onsensorchanged!", Toast.LENGTH_SHORT).show();

        if(running)
        {
            totalSteps = (int) event.values[0];
            int currentSteps = totalSteps - previousTotalSteps;

            TextView stepsCounter = findViewById(R.id.steps_counter);
            stepsCounter.setText(currentSteps);

            CircularProgressBar progressCircular = findViewById(R.id.circularProgressBar);
            progressCircular.setProgressWithAnimation(currentSteps);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){}

    private void resetSteps()
    {
        TextView stepsText = findViewById(R.id.steps_text);
        stepsText.setOnClickListener(unused -> Toast.makeText(this, "Long tap to reset!", Toast.LENGTH_SHORT).show());
        stepsText.setOnLongClickListener(unused ->
        {
            previousTotalSteps = totalSteps;
            TextView stepsCounter = findViewById(R.id.steps_counter);
            stepsCounter.setText(0);
            saveData();

            return true;
        });
    }

    private void saveData()
    {
        SharedPreferences prefs = getSharedPreferences("pedometerPrefs", Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putInt("steps", previousTotalSteps);
        editor.apply();
    }

    private void loadData()
    {
        SharedPreferences prefs = getSharedPreferences("pedometerPrefs", Context.MODE_PRIVATE);
        previousTotalSteps = prefs.getInt("steps", 0);
    }

    private SensorManager sensorManager;

    private boolean running = false;
    private int totalSteps = 0;
    private int previousTotalSteps = 0;
}
