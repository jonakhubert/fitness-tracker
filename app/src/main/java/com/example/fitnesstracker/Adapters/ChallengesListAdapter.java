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

public class ChallengesListAdapter extends BaseAdapter
{
    public Context context;
    public LayoutInflater inflater;

    public ChallengesListAdapter(Context context, String[] challengesNames, int[] challengesMilestones)
    {
        this.context = context;
        this.challengesNames = challengesNames;
        this.challengesMilestones = challengesMilestones;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return challengesNames.length;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

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

    private String[] challengesNames;
    private int[] challengesMilestones;
}
