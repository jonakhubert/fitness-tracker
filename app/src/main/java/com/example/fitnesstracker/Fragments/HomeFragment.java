package com.example.fitnesstracker.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnesstracker.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class HomeFragment extends Fragment {
    private View view;
    private AdView ad;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ad = view.findViewById(R.id.adView);

        MobileAds.initialize(requireActivity(), initializationStatus -> {});

        AdRequest adRequest = new AdRequest.Builder().build();
        ad.loadAd(adRequest);

        return view;
    }
}