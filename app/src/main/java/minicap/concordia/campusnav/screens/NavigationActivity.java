package minicap.concordia.campusnav.screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.components.MainMenuDialog;
import minicap.concordia.campusnav.map.AbstractMap;
import minicap.concordia.campusnav.map.MapCoordinates;
import minicap.concordia.campusnav.map.NavigationGoogleMaps;
import minicap.concordia.campusnav.map.enums.MapColors;

public class NavigationActivity extends AppCompatActivity implements AbstractMap.MapUpdateListener, MainMenuDialog.MainMenuListener {

    private static final int LOCATION_REQUEST_CODE = 101;
    public static final float DEFAULT_ZOOM = 18f;
    private static final float ROUTE_UPDATE_DISTANCE_THRESHOLD = 50;

    private AbstractMap curMap;
    private Fragment curMapFragment;
    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;

    private MapCoordinates origin;
    private MapCoordinates destination;

    private String travelMode;

    private TextView etaText;
    private TextView statsText;
    private ImageButton exit;
    private ImageButton mainMenu;
    private boolean isNavigationActive = false;
    private MapCoordinates lastRouteUpdatePosition;

    /**
     * On create method, runs when activity is created
     * @param savedInstanceState bundle data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set layout to activity navigation
        setContentView(R.layout.activity_navigation);

        //Run routine
        initializeViews();
        getRouteDataFromIntent();
        setupLocationClient();
        initializeMap();
    }

    /**
     * Initializing the UI components
     */
    private void initializeViews() {
        //Grabing UI elements from the layout
        etaText = findViewById(R.id.eta_text);
        statsText = findViewById(R.id.statsText);
        mainMenu = findViewById(R.id.mainMenu);
        exit = findViewById(R.id.exitButton);

        //Setting up necessary buttons
        mainMenu.setOnClickListener(v -> showMainMenuDialog());
        exit.setOnClickListener(v -> exitIntent());
    }

    private void getRouteDataFromIntent() {
        try {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                throw new IllegalStateException("No extras in intent");
            }

            origin = new MapCoordinates(extras.getDouble("origin_lat"), extras.getDouble("origin_lng"));
            destination = new MapCoordinates(extras.getDouble("destination_lat"), extras.getDouble("destination_lng"));
            travelMode = extras.getString("travel_mode", "WALK");

        } catch (Exception e) {
            Log.e("Navigation", "Error parsing intent data", e);
            Toast.makeText(this, "Invalid navigation data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupLocationClient() {
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {

                    updateUserPosition(MapCoordinates.fromAndroidLocation(location));
                }
            }
        };
    }

    /**
     * Initializing the map through the activity
     */
    private void initializeMap() {
        //set the curMap variable to an Internal Google maps
        curMap = new NavigationGoogleMaps(this);

        //get fragment from initialized variable
        curMapFragment = curMap.initialize();

        getSupportFragmentManager().beginTransaction().add(R.id.navigation_map, curMapFragment)
                .commit();

    }

    @Override
    public void onMapReady() {
        try {
            //Set map style
            curMap.setStyle(this,R.raw.nav_map_style);
            //setup map markers
            setupMapMarkers();

            fetchAndDisplayRoute();
            checkLocationPermissions();

        } catch (Exception e) {
            Log.e("Navigation", "Map ready error", e);
            Toast.makeText(this, "Map setup failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupMapMarkers() {
        curMap.addMarker(
                destination,
                "Destination",
                MapColors.DEFAULT,
                true
        );
    }

    private void fetchAndDisplayRoute() {
        if (origin == null || destination == null) {
            Toast.makeText(this, "Location data not available", Toast.LENGTH_SHORT).show();
            return;
        }
        curMap.displayRoute(origin, destination, travelMode);
    }

    private void fetchAndDisplayRoute(MapCoordinates origin, MapCoordinates destination) {
        curMap.displayRoute(origin, destination, travelMode);
        isNavigationActive = true;
    }


    private void updateUserPosition(MapCoordinates location) {

        float remainingDistance = curMap.calculateRemainingDistance(location);
        updateStatsText((int) remainingDistance);

        float pathBearing = curMap.calculatePathBearing(location);
        updateUserMarker(location);
        updateCameraPosition(location, pathBearing);

        if (isNavigationActive && shouldUpdateRoute(location)) {
            lastRouteUpdatePosition = location;
            fetchAndDisplayRoute(location, destination);
        }
    }

    private void updateUserMarker(MapCoordinates position) {
        curMap.updateUserMarkerPosition(position, this);
    }

    private void updateCameraPosition(MapCoordinates position, float bearing) {
        float markerRotation = (bearing + 110) % 360;

        curMap.rotateUserMarker(markerRotation);

        int padding = (int)(getResources().getDisplayMetrics().heightPixels * 0.3);

        curMap.moveCameraToPosition(padding, position, DEFAULT_ZOOM, bearing);

    }

    private boolean shouldUpdateRoute(MapCoordinates currentPosition) {
        if (lastRouteUpdatePosition == null) {
            return true;
        }

        float[] results = new float[1];
        Location.distanceBetween(currentPosition.getLat(), currentPosition.getLng(), lastRouteUpdatePosition.getLat(), lastRouteUpdatePosition.getLng(), results);
        return results[0] > ROUTE_UPDATE_DISTANCE_THRESHOLD;
    }

    private String formatArrivalTime(String etaDuration) {
        try {
            if (etaDuration.equals("N/A")) {
                return "";
            }

            int minutes = Integer.parseInt(etaDuration.replaceAll("[^0-9]", ""));
            long arrivalMillis = System.currentTimeMillis() + (minutes * 60 * 1000L);

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
            return sdf.format(new Date(arrivalMillis));

        } catch (Exception e) {
            Log.e("Navigation", "Error formatting arrival time", e);
            return etaDuration;
        }
    }

    private void updateStatsText(int distanceMeters) {
        String etaDuration = etaText.getText().toString().replaceAll("[^0-9]", "");
        String arrivalTime = formatArrivalTime(etaDuration);

        String distanceText;
        if (distanceMeters >= 1000) {
            distanceText = String.format("%.1f km", distanceMeters / 1000.0);
        } else {
            distanceText = String.format("%d m", distanceMeters);
        }

        String stats = String.format("%s • %s", distanceText, arrivalTime);
        statsText.setText(stats);
    }


    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    private void startLocationUpdates() {
        try {
            LocationRequest locationRequest = new LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, 1000).setMinUpdateIntervalMillis(500).build();

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            }
        } catch (Exception e) {
            Log.e("Navigation", "Location updates error", e);
        }
    }

    private void stopLocationUpdates() {
        try {
            locationClient.removeLocationUpdates(locationCallback);
        } catch (Exception e) {
            Log.e("Navigation", "Stop updates error", e);
        }
    }

    private void startNavigation() {
        if (origin == null || destination == null) {
            Toast.makeText(this, "Please set both origin and destination", Toast.LENGTH_SHORT).show();
            return;
        }

        fetchAndDisplayRoute(origin, destination);
        isNavigationActive = true;
    }

    private void stopNavigation() {
        isNavigationActive = false;
        lastRouteUpdatePosition = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopNavigation();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();

            if (origin != null && destination != null) {
                startNavigation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission required for navigation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showMainMenuDialog() {
        MainMenuDialog dialog = new MainMenuDialog(this);
        dialog.show();
    }

    public void exitIntent(){
        stopNavigation();
        stopLocationUpdates();

        Intent i = new Intent();
        i.putExtra("OPEN_DIR", true);
        setResult(RESULT_OK, i);
        finish();
    }


    @Override
    public void onEstimatedTimeUpdated(String newTime) {
        etaText.setText(getString(R.string.eta_format, newTime));

    }

    @Override
    public void onMapError(String errorString) {

    }

    @Override
    public void onMapClicked(MapCoordinates coordinates) {

    }
}