package com.example.fitnesstracker.Activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.Adapters.ChallengesListAdapter;
import com.example.fitnesstracker.R;

public class ChallengesActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        ChallengesListAdapter adapter = new ChallengesListAdapter(getApplicationContext(), challengesNames, challengesMilestones);
        listView = findViewById(R.id.challengesList);
        listView.setAdapter(adapter);
    }

    public static int getLastMilestone()
    {
        return challengesMilestones[challengesMilestones.length - 1];
    }

    public static int getLastIncompletedMilestone()
    {
        for(int milestone : challengesMilestones)
        {
            if(milestone >= MapActivity.steps)
            {
                return milestone;
            }
        }

        return getLastMilestone();
    }

    private String[] challengesNames = {"first steps", "fitness enjoyer", "fitness beast"};
    private static int[] challengesMilestones = {10, 20, 30};
    private ListView listView;
}
