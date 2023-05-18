package com.example.fitnesstracker.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnesstracker.Activities.ChallengesActivity;
import com.example.fitnesstracker.Activities.MapActivity;
import com.example.fitnesstracker.Activities.MusicActivity;
import com.example.fitnesstracker.Activities.PedometerActivity;
import com.example.fitnesstracker.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Fragment class for the home screen.
 */
public class HomeFragment extends Fragment
{
    /**
     * The root view of the fragment.
     */
    private View view;
    /**
     * The AdView for displaying ads.
     */
    private AdView ad;
    /**
     * CardView for the pedometer functionality.
     */
    private CardView pedometerCard;
    /**
     * CardView for the challenges functionality.
     */
    private CardView challengesCard;
    /**
     * CardView for the map functionality.
     */
    private CardView mapCard;
    /**
     * CardView for the music functionality.
     */
    private CardView musicCard;

    /**
     * Required empty public constructor
     */
    public HomeFragment() {}

    /**
     * Called when the fragment's view is created.
     * @param inflater The layout inflater.
     * @param container The container for the fragment's view.
     * @param savedInstanceState The saved instance state bundle.
     * @return The created view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ad = view.findViewById(R.id.adView);
        pedometerCard = view.findViewById(R.id.pedometer);
        challengesCard = view.findViewById(R.id.challenges);
        mapCard = view.findViewById(R.id.map);
        musicCard = view.findViewById(R.id.music);

        MobileAds.initialize(requireActivity(), initializationStatus -> {});

        AdRequest adRequest = new AdRequest.Builder().build();
        ad.loadAd(adRequest);

        pedometerCard.setOnClickListener(unused ->
        {
            Intent intent = new Intent(requireActivity(), PedometerActivity.class);
            startActivity(intent);
        });

        challengesCard.setOnClickListener(unused ->
        {
            Intent intent = new Intent(requireActivity(), ChallengesActivity.class);
            startActivity(intent);
        });

        mapCard.setOnClickListener(v -> {
            Intent intent;
            if (MapActivity.getInstance() != null) {
                Log.d("HomeFragment", "Using existing instance of MapActivity");
                intent = new Intent(requireActivity(), MapActivity.getInstance().getClass());
            } else {
                Log.d("HomeFragment", "Creating new instance of MapActivity");
                intent = new Intent(requireActivity(), MapActivity.class);
            }
            startActivity(intent);
        });

        musicCard.setOnClickListener(unused ->
        {
            Intent intent;
            if (MusicActivity.getInstance() != null) {
                Log.d("HomeFragment", "Using existing instance of MusicActivity");
                intent = new Intent(requireActivity(), MusicActivity.getInstance().getClass());
            } else {
                Log.d("HomeFragment", "Creating new instance of MusicActivity");
                intent = new Intent(requireActivity(), MusicActivity.class);
            }
            startActivity(intent);
        });

        return view;
    }
}