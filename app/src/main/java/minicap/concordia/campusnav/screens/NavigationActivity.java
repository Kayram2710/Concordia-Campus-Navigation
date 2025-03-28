package minicap.concordia.campusnav.screens;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.map.FetchPathTask;
import minicap.concordia.campusnav.map.MapCoordinates;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback, FetchPathTask.OnRouteFetchedListener {

    private static final int LOCATION_REQUEST_CODE = 101;
    private static final float DEFAULT_ZOOM = 18f;
    private static final float ROUTE_ZOOM = 15f;
    private static final float ROUTE_UPDATE_DISTANCE_THRESHOLD = 50;


    private GoogleMap googleMap;
    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;

    private MapCoordinates origin;
    private MapCoordinates destination;
    private Marker userMarker;
    private String travelMode;

    private Marker originMarker;
    private Marker destinationMarker;
    private List<Polyline> routePolylines = new ArrayList<>();
    private TextView etaText;
    private JSONArray routeData;
    private boolean isNavigationActive = false;
    private LatLng lastRouteUpdatePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        initializeViews();
        getRouteDataFromIntent();
        setupLocationClient();
        initializeMap();
    }

    private void initializeViews() {
        etaText = findViewById(R.id.eta_text);
    }

    private void getRouteDataFromIntent() {
        try {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                throw new IllegalStateException("No extras in intent");
            }

            origin = new MapCoordinates(
                    extras.getDouble("origin_lat"),
                    extras.getDouble("origin_lng")
            );
            destination = new MapCoordinates(
                    extras.getDouble("destination_lat"),
                    extras.getDouble("destination_lng")
            );
            travelMode = extras.getString("travel_mode", "WALK");

            String routeJson = extras.getString("route_data");
            if (routeJson != null) {
                routeData = new JSONArray(routeJson);
            }
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
                    updateUserPosition(location);
                }
            }
        };
    }

    private void initializeMap() {
        try {
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.navigation_map, mapFragment).commit();
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Log.e("Navigation", "Map init error", e);
            Toast.makeText(this, "Map initialization failed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            this.googleMap = googleMap;
            configureMap();
            setupMapMarkers();

            if (routeData != null) {
                onRouteFetched(routeData);
            } else {
                fetchAndDisplayRoute();
            }

            checkLocationPermissions();
        } catch (Exception e) {
            Log.e("Navigation", "Map ready error", e);
            Toast.makeText(this, "Map setup failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void configureMap() {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.nav_map_style));
            if (!success) {
                Log.w("Navigation", "Map style parsing failed");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Navigation", "Map style not found", e);
        }
    }

    private void setupMapMarkers() {
        if (destinationMarker != null) destinationMarker.remove();

        destinationMarker = googleMap.addMarker(new MarkerOptions().position(destination.toGoogleMapsLatLng()).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    private void fetchAndDisplayRoute() {
        if (googleMap == null || origin == null || destination == null) {
            Toast.makeText(this, "Location data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        new FetchPathTask(this).fetchRoute(origin.toGoogleMapsLatLng(), destination.toGoogleMapsLatLng(), travelMode);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin.toGoogleMapsLatLng(), ROUTE_ZOOM));
    }

    private void updateUserPosition(Location location) {
        if (location == null || googleMap == null) return;

        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        float bearing = location.hasBearing() ? location.getBearing() : 0f;

        updateUserMarker(userLatLng);

        updateCameraPosition(userLatLng, bearing);

        if (isNavigationActive && shouldUpdateRoute(userLatLng)) {
            lastRouteUpdatePosition = userLatLng;
            fetchAndDisplayRoute(userLatLng, destination.toGoogleMapsLatLng());
        }
    }

    private void updateUserMarker(LatLng position) {
        if (userMarker == null) {
            BitmapDescriptor icon = bitmapDescriptorFromVector(this, R.drawable.ic_person);
            if (icon == null) {
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
            }

            userMarker = googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title("Your Location")
                    .icon(icon)
                    .flat(true));
        } else {
            userMarker.setPosition(position);
        }
    }

    private void updateCameraPosition(LatLng position, float bearing) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(DEFAULT_ZOOM).bearing(bearing).tilt(45).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private boolean shouldUpdateRoute(LatLng currentPosition) {
        if (lastRouteUpdatePosition == null) {
            return true;
        }

        float[] results = new float[1];
        Location.distanceBetween(currentPosition.latitude, currentPosition.longitude, lastRouteUpdatePosition.latitude, lastRouteUpdatePosition.longitude, results);
        return results[0] > ROUTE_UPDATE_DISTANCE_THRESHOLD;
    }

    private void fetchAndDisplayRoute(LatLng origin, LatLng destination) {
        if (googleMap == null) return;

        new FetchPathTask(this).fetchRoute(origin, destination, travelMode);
        isNavigationActive = true;
    }

    @Override
    public void onRouteFetched(JSONArray routeInfo) {
        runOnUiThread(() -> {
            try {
                if (routeInfo == null || routeInfo.length() < 2) {
                    throw new JSONException("Invalid route data");
                }

                clearRoute();

                JSONArray steps = routeInfo.getJSONArray(0);
                String eta = routeInfo.optString(1, "N/A");

                etaText.setText(getString(R.string.eta_format, eta));
                displayRouteSteps(steps);

            } catch (Exception e) {
                Log.e("Navigation", "Route display error", e);
                Toast.makeText(NavigationActivity.this,
                        "Error updating route", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayRouteSteps(JSONArray steps) {
        clearRoute();
        List<LatLng> allPoints = new ArrayList<>();

        for (int i = 0; i < steps.length(); i++) {
            try {
                JSONObject step = steps.getJSONObject(i);
                JSONObject polyline = step.getJSONObject("polyline");
                String encodedPath = polyline.getString("encodedPolyline");
                allPoints.addAll(PolyUtil.decode(encodedPath));
            } catch (JSONException e) {
                Log.e("RouteDisplay", "Error parsing step " + i, e);
            }
        }

        if (!allPoints.isEmpty()) {
            PolylineOptions options = new PolylineOptions().addAll(allPoints).width(20).color(Color.parseColor("#4285F4")).geodesic(true).zIndex(10);

            routePolylines.add(googleMap.addPolyline(options));
            zoomToRoute();
        }
    }

    private void clearRoute() {
        for (Polyline polyline : routePolylines) {
            polyline.remove();
        }
        routePolylines.clear();
    }

    private void zoomToRoute() {
        if (googleMap == null || routePolylines.isEmpty()) return;

        try {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Polyline polyline : routePolylines) {
                for (LatLng point : polyline.getPoints()) {
                    builder.include(point);
                }
            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        } catch (IllegalStateException e) {
            googleMap.setOnMapLoadedCallback(() -> {
                try {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Polyline polyline : routePolylines) {
                        for (LatLng point : polyline.getPoints()) {
                            builder.include(point);
                        }
                    }
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                            builder.build(), 100));
                } catch (Exception ex) {
                    Log.e("Navigation", "Zoom error", ex);
                }
            });
        } catch (Exception e) {
            Log.e("Navigation", "Zoom error", e);
        }
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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        try {
            Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
            if (vectorDrawable == null) {
                return null;
            }
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        } catch (Exception e) {
            Log.e("Navigation", "Vector icon error", e);
            return null;
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

        fetchAndDisplayRoute(origin.toGoogleMapsLatLng(), destination.toGoogleMapsLatLng());
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
}