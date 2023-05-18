package com.example.fitnesstracker.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fitnesstracker.Activities.ChallengesActivity;
import com.example.fitnesstracker.Activities.MapActivity;
import com.example.fitnesstracker.Activities.PedometerActivity;
import com.example.fitnesstracker.R;

/**
 * Adapter class for displaying challenges in a ListView.
 */
public class ChallengesListAdapter extends BaseAdapter
{
    /**
     * The context of the adapter.
     */
    public Context context;
    /**
     * Inflater used to inflate the layout for each item.
     */
    public LayoutInflater inflater;
    /**
     * Array of challenge names.
     */
    private String[] challengesNames;
    /**
     * Array of challenge milestones.
     */
    private int[] challengesMilestones;

    /**
     * Constructs a ChallengesListAdapter.
     *
     * @param context           The context of the adapter.
     * @param challengesNames   An array of challenge names.
     * @param challengesMilestones An array of challenge milestones.
     */
    public ChallengesListAdapter(Context context, String[] challengesNames, int[] challengesMilestones)
    {
        this.context = context;
        this.challengesNames = challengesNames;
        this.challengesMilestones = challengesMilestones;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Get the number of items in the data set represented by this adapter.
     *
     * @return The total number of items in the data set.
     */
    @Override
    public int getCount()
    {
        return challengesNames.length;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position The position of the item within the adapter's data set.
     * @return The data item at the specified position, or null if the position is out of range.
     */
    @Override
    public Object getItem(int position)
    {
        return null;
    }

    /**
     * Returns the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return The View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflater.inflate(R.layout.listview_challenges, null);
        TextView challengeName = (TextView) convertView.findViewById(R.id.challengeName);
        TextView challengeMilestone = (TextView) convertView.findViewById(R.id.challengeMilestone);
        challengeName.setText(challengesNames[position]);
        challengeMilestone.setText(String.valueOf(challengesMilestones[position]));

        if(MapActivity.steps >= challengesMilestones[position])
        {
            challengeName.setTextColor(Color.parseColor("#00FF00"));
            challengeMilestone.setTextColor(Color.parseColor("#00FF00"));
        }
        else
        {
            challengeName.setTextColor(Color.parseColor("#FF0000"));
            challengeMilestone.setTextColor(Color.parseColor("#FF0000"));
        }

        return convertView;
    }
}