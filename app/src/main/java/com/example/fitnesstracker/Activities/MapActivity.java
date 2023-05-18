package com.example.fitnesstracker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fitnesstracker.R;

import com.example.fitnesstracker.Util.NotificationUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * Represents the MapActivity class, responsible for displaying the map and tracking user location.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    // if there is an error: Error inflating class fragment -> clear the application cache in the emulator settings
    /**
     * Final variable for permission access
     */
    private final int FINE_PERMISSION_CODE = 1;
    /**
     * GoogleMap instance for displaying the map
     */
    private GoogleMap myMap;
    /**
     * Current user location
     */
    private Location currentLocation;
    /**
     * FusedLocationProviderClient for accessing location services
     */
    private FusedLocationProviderClient fusedLocationProviderClient;
    /**
     * LocationCallback for receiving location updates
     */
    private LocationCallback locationCallback;
    /**
     * Firebase authentication instance
     */
    private FirebaseAuth firebaseAuth;
    /**
     * Current Firebase user
     */
    private FirebaseUser user;
    /**
     * Singleton instance of MapActivity
     */
    public static MapActivity instance;
    /**
     * Step count
     */
    public static int steps = 0;

    /**
     * Get the singleton instance of MapActivity.
     * @return The MapActivity instance.
     */
    public static MapActivity getInstance()
    {
        return instance;
    }

    /**
     * Called when the activity is created.
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        instance = this;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        getLastLocation();
    }

    /**
     * Called when the activity is resumed.
     * It requests the user's last known location.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        getLastLocation();
    }

    /**
     * Retrieves the user's last known location and updates the map and step count accordingly.
     * If location permissions are not granted, it requests the necessary permissions.
     */
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location newLocation = locationResult.getLastLocation();
                LatLng myLocation = new LatLng(Objects.requireNonNull(newLocation).getLatitude(), newLocation.getLongitude());
                if (myMap != null) {
                    myMap.clear();
                    myMap.addMarker(new MarkerOptions().position(myLocation).title("MyLocation"));
                    myMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                }

                updateStepCount(newLocation);
                Log.d("StepCounter", "Current Step Counter: " + steps);
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if(location != null)
            {
                currentLocation = location;
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                Objects.requireNonNull(mapFragment).getMapAsync(MapActivity.this);
            }
        });
    }

    /**
     * Called when the map is ready to be used.
     * It sets up the map with the user's current location, enables zoom controls and compass,
     * and updates the step count based on the current location.
     *
     * @param googleMap The GoogleMap object representing the map.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        myMap.addMarker(new MarkerOptions().position(myLocation).title("MyLocation"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

        LocationRequest locationRequest = new LocationRequest.Builder(1000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

        updateStepCount(currentLocation);
    }

    /**
     * Called when the app receives the result of a permission request.
     * It checks if the fine location permission is granted, and if so, it retrieves the user's last location.
     * If the permission is denied, it displays a toast message.
     *
     * @param requestCode The code that was used to request the permission.
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation();
            }
            else
            {
                Toast.makeText(this, "Location permission is denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Updates the step count based on the current location.
     * If the current location is significantly different from the previous location,
     * it increments the step count and checks if any challenges have been completed.
     * If a challenge is completed, it sends a notification.
     *
     * @param newLocation The new location to update the step count.
     */
    private void updateStepCount(Location newLocation) {
        if (currentLocation != null && currentLocation.distanceTo(newLocation) > 1) {
            steps++;

            Log.d("Steps", "steps: " + steps + " milestone: " + ChallengesActivity.getLastIncompletedMilestone());

            int index = ChallengesActivity.getLastIndex();

            if(steps >= ChallengesActivity.getLastIncompletedMilestone() && index < ChallengesActivity.challengesNames.length)
            {
                Log.d("Steps", "Challange " + ChallengesActivity.getLastIncompletedMilestone() + " completed!");
                NotificationUtil.sendNotification(this, "Challenge completed!",
                "You reached " + ChallengesActivity.challengesMilestones[index] +
                " steps and completed " +
                ChallengesActivity.challengesNames[index] + " challenge!");
            }
        }
        currentLocation = newLocation;
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}