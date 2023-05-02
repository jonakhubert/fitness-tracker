package com.example.fitnesstracker.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnesstracker.Activities.MapActivity;
import com.example.fitnesstracker.Activities.PedometerActivity;
import com.example.fitnesstracker.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class HomeFragment extends Fragment {
    private View view;
    private AdView ad;
    private CardView pedometerCard;
    private CardView challengesCard;
    private CardView mapCard;
    private CardView musicCard;

    public HomeFragment() {
        // Required empty public constructor
    }

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

        mapCard.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MapActivity.class);
            startActivity(intent);
        });

        pedometerCard.setOnClickListener(unused ->
        {
            Intent intent = new Intent(requireActivity(), PedometerActivity.class);
            startActivity(intent);
        });

        return view;
    }
}