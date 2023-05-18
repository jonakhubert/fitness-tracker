package com.example.fitnesstracker.Activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.Adapters.ChallengesListAdapter;
import com.example.fitnesstracker.R;

/**
 * Represents the ChallengesActivity class, which is responsible for managing the challenges.
 */
public class ChallengesActivity extends AppCompatActivity
{
    /**
     * Called when the activity is created.
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        ChallengesListAdapter adapter = new ChallengesListAdapter(getApplicationContext(), challengesNames, challengesMilestones);
        listView = findViewById(R.id.challengesList);
        listView.setAdapter(adapter);
    }

    /**
     * Get the value of the last milestone.
     * @return The value of the last milestone.
     */
    public static int getLastMilestone()
    {
        return challengesMilestones[challengesMilestones.length - 1];
    }

    /**
     * Get the value of the last incomplete milestone based on the current number of steps.
     * @return The value of the last incomplete milestone.
     */
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

    /**
     * Get the index of the last completed milestone.
     * @return The index of the last completed milestone.
     */
    public static int getLastIndex()
    {
        int index = 0;

        for(int milestone : challengesMilestones)
        {
            if(milestone >= MapActivity.steps)
            {
                break;
            }
            ++index;
        }

        return index;
    }

    /**
     * Array containing the names of the challenges.
     */
    public static String[] challengesNames = {"first steps", "fitness enjoyer", "fitness beast"};
    /**
     * Array containing the milestones for the challenges
     */
    public static int[] challengesMilestones = {10, 20, 30};
    /**
     * ListView control storing challenges names and milestones
     */
    private ListView listView;
}
