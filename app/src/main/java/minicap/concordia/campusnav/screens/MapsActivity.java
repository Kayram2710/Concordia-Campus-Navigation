package minicap.concordia.campusnav.screens;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import android.widget.EditText;


import com.google.android.material.bottomsheet.BottomSheetBehavior;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingshape.CampusBuildingShapes;
import minicap.concordia.campusnav.databinding.ActivityMapsBinding;
import minicap.concordia.campusnav.map.FetchPathTask;
import minicap.concordia.campusnav.map.InternalGoogleMaps;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, FetchPathTask.OnRouteFetchedListener{

    private final String MAPS_ACTIVITY_TAG = "MapsActivity";

    public static final String KEY_STARTING_LAT = "starting_lat";
    public static final String KEY_STARTING_LNG = "starting_lng";
    public static final String KEY_CAMPUS_NOT_SELECTED = "campus_not_selected";

    public static final String KEY_SHOW_SGW = "show_sgw";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private final LatLng defaultUserLocation = new LatLng(45.489682435037835, -73.58808030276997);

    private InternalGoogleMaps gMapController;

    private ActivityMapsBinding binding;

    private ConcordiaBuildingManager buildingManager;

    private double startingLat;

    private double startingLng;

    private boolean isDestinationSet;

    private boolean showSGW;

    private boolean hasUserLocationBeenSet;

    private Button campusSwitchBtn;

    private TextView campusTextView;

    private String campusNotSelected;

    private EditText yourLocationEditText;

    private EditText destinationEditText;

    private ImageButton walkButton;
    private ImageButton wheelchairButton;
    private ImageButton carButton;
    private ImageButton transitButton;

    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;

    private FusedLocationProviderClient fusedLocationClient;

    private String travelMode = "DRIVE";

    private LatLng destination;

    private LatLng startingPoint;

    private ActivityResultLauncher<Intent> searchLocationLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isDestinationSet = false;
        hasUserLocationBeenSet = false;
        buildingManager = ConcordiaBuildingManager.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            startingLat = bundle.getDouble(KEY_STARTING_LAT);
            startingLng = bundle.getDouble(KEY_STARTING_LNG);
            campusNotSelected = bundle.getString(KEY_CAMPUS_NOT_SELECTED);
            showSGW = bundle.getBoolean(KEY_SHOW_SGW);
        }

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        int peekHeightPx = (int) (32 * getResources().getDisplayMetrics().density);
        bottomSheetBehavior.setPeekHeight(peekHeightPx); // Set peek height
        bottomSheetBehavior.setHideable(false); // Prevent complete hiding
        bottomSheetBehavior.setFitToContents(true); // Lock to collapsed or expanded state
        bottomSheetBehavior.setHalfExpandedRatio(0.01f); // Disable half-expanded state
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        campusTextView = findViewById(R.id.ToCampus);
        campusTextView.setText(campusNotSelected);
        campusSwitchBtn = findViewById(R.id.campusSwitch);

        campusSwitchBtn.setOnClickListener(v -> toggleCampus());

        // check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // request perm
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // start map
            initializeMap();
        }

        walkButton = findViewById(R.id.walkButton);
        wheelchairButton = findViewById(R.id.wheelchairButton);
        carButton = findViewById(R.id.carButton);
        transitButton = findViewById(R.id.transitButton);

        //Default mode
        carButton.setSelected(true);

        walkButton.setOnClickListener(v -> changeSelectedTravelMethod(walkButton, "WALK"));

        //google maps does not support "wheelchair" for now, travelMode is the same as walking
        wheelchairButton.setOnClickListener(v -> changeSelectedTravelMethod(wheelchairButton, "WALK"));

        carButton.setOnClickListener(v -> changeSelectedTravelMethod(carButton, "DRIVE"));

        transitButton.setOnClickListener(v -> changeSelectedTravelMethod(transitButton, "TRANSIT"));

        yourLocationEditText = findViewById(R.id.yourLocationEditText);

        TextInputEditText searchText = findViewById(R.id.genericSearchField);

        //Add main menu functionality to page
        View slidingMenu = findViewById(R.id.sliding_menu);
        ImageButton openMenuButton = findViewById(R.id.menuButton);
        ImageButton closeMenuButton = findViewById(R.id.closeMenu);
        ImageButton classScheduleRedirect = findViewById(R.id.classScheduleRedirect);
        ImageButton directionsRedirect = findViewById(R.id.directionsRedirect);
        ImageButton campusMapRedirect = findViewById(R.id.campusMapRedirect);
        MainMenuController menu = new MainMenuController(this, slidingMenu, openMenuButton, closeMenuButton, classScheduleRedirect, directionsRedirect, campusMapRedirect);


        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    launchSearchActivity("", false);
                }
            }
        });

        yourLocationEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    launchSearchActivity(yourLocationEditText.getText().toString(), true);
                }
            }
        });

        destinationEditText = findViewById(R.id.destinationText);

        destinationEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    launchSearchActivity(destinationEditText.getText().toString(), false);
                }
            }
        });

        searchLocationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == LocationSearchActivity.RESULT_OK) {
                            Intent intent = result.getData();
                            if(intent == null) {
                                Log.e(MAPS_ACTIVITY_TAG, "Error while reading results from search, no intent returned");
                                return;
                            }
                            Bundle returnData = intent.getExtras();
                            if(returnData == null) {
                                Log.e(MAPS_ACTIVITY_TAG, "Error while reading results from search, no data was returned");
                                return;
                            }

                            boolean isDestination = returnData.getBoolean(LocationSearchActivity.KEY_RETURN_BOOL_IS_DESTINATION);
                            float lat = returnData.getFloat(LocationSearchActivity.KEY_RETURN_CHOSEN_LAT);
                            float lng = returnData.getFloat(LocationSearchActivity.KEY_RETURN_CHOSEN_LNG);
                            if(isDestination) {
                                String returnedLocation = returnData.getString(LocationSearchActivity.KEY_RETURN_CHOSEN_LOCATION);
                                setDestination(returnedLocation, lat, lng);
                            }
                            else {
                                boolean useCurrentLocation = returnData.getBoolean(LocationSearchActivity.KEY_RETURN_BOOL_CURRENT_LOCATION);
                                if(useCurrentLocation) {
                                    setStartingPoint(true, "", lat, lng);
                                }
                                else {
                                    String returnedLocation = returnData.getString(LocationSearchActivity.KEY_RETURN_CHOSEN_LOCATION);
                                    setStartingPoint(false, returnedLocation, lat, lng);
                                }
                            }
                        }
                        runOnUiThread(() -> {
                            searchText.clearFocus();
                            destinationEditText.clearFocus();
                            yourLocationEditText.clearFocus();
                        });
                    }
                });

        getUserLocationPath();
    }

    /**
     * Launches the search activity and sets the extra parameters
     * @param previousInput The previous location that was selected if there was one
     * @param isStartingLocation Flag to indicate if this is a search for starting location or destination
     */
    private void launchSearchActivity(String previousInput, boolean isStartingLocation) {
        Intent i = new Intent(MapsActivity.this, LocationSearchActivity.class);
        i.putExtra(LocationSearchActivity.KEY_IS_STARTING_LOCATION, isStartingLocation);
        i.putExtra(LocationSearchActivity.KEY_PREVIOUS_INPUT_STRING, previousInput);

        searchLocationLauncher.launch(i);
    }

    /**
     * Sets the starting point used for navigation
     * @param useCurrentLocation Flag indicating whether to use the current location of the user
     * @param locationString The name of the location
     * @param lat The latitude of the location
     * @param lng The longitude of the location
     */
    private void setStartingPoint(boolean useCurrentLocation, String locationString, float lat, float lng) {
        Log.d(MAPS_ACTIVITY_TAG, "Set starting location to: " + locationString + " with coords: (" + lat + ", " + lng + "), is current location: " + useCurrentLocation);

        if(useCurrentLocation && !hasUserLocationBeenSet) {
            //getUserLocationPath will set the starting point and then call this again
            getUserLocationPath();
            return;
        }

        if(useCurrentLocation) {
            yourLocationEditText.setText(R.string.your_location);
        }
        else {
            //Reset this flag in case the user wants to use their own location in the future
            hasUserLocationBeenSet = false;
            startingPoint = new LatLng(lat, lng);
            yourLocationEditText.setText(locationString);
        }

        drawPath(startingPoint, destination);
    }

    /**
     * Sets the destination used for navigation
     * @param locationString The name of the location
     * @param lat The location's latitude
     * @param lng The location's longitude
     */
    private void setDestination(String locationString, float lat, float lng) {
        Log.d(MAPS_ACTIVITY_TAG, "Set starting location to: " + locationString + " with coords: (" + lat + ", " + lng + ")");

        destination = new LatLng(lat, lng);
        destinationEditText.setText(locationString);
        isDestinationSet = true;

        drawPath(startingPoint, destination);
    }

    /**
     * Toggles the map to either the SGW campus or the Loyola campus based on which was already focused
     */
    private void toggleCampus(){
        //flipping the state
        showSGW = !showSGW;

        // getting the new campus location
        CampusName wantedCampus = showSGW ? CampusName.SGW : CampusName.LOYOLA;
        Campus curCampus = buildingManager.getCampus(wantedCampus);
        LatLng campusCoords = new LatLng(curCampus.getLocation()[0], curCampus.getLocation()[1]);

        //moving the existing marker to the new campus location
        gMapController.addMarker(campusCoords, curCampus.getCampusName(), true);

        //moving the camera smoothly to the new campus location
        gMapController.centerOnCoordinates(campusCoords.latitude, campusCoords.longitude);

        //updating the button text
        campusTextView.setText(showSGW ? "SGW" : "LOY");
    }

    /**
     * Switches the desired travel method and re-draws the route line
     * @param selectedButton The travel method button that was clicked
     * @param newTravelMode The new travel mode to be used
     */
    private void changeSelectedTravelMethod(ImageButton selectedButton, String newTravelMode) {
        walkButton.setSelected(false);
        wheelchairButton.setSelected(false);
        carButton.setSelected(false);
        transitButton.setSelected(false);

        selectedButton.setSelected(true);
        travelMode = newTravelMode;

        drawPath(startingPoint, destination);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start map
                initializeMap();
            } else {
                // error if no perm
                Toast.makeText(this, "Location permission is required to show your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Initializes google maps
     */
    private void initializeMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Callback for when google maps has loaded
     * @param googleMap The loaded google map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMapController = new InternalGoogleMaps(googleMap);

        gMapController.centerOnCoordinates(startingLat, startingLng);

        // create building shapes
        gMapController.addPolygons(CampusBuildingShapes.getSgwBuildingCoordinates());
        gMapController.addPolygons(CampusBuildingShapes.getLoyolaBuildingCoordinates());

        gMapController.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                String address = getAddressFromLocation(latLng.latitude, latLng.longitude);
                gMapController.addMarker(latLng, "Clicked location", true);
                setDestination(address, (float)latLng.latitude, (float)latLng.longitude);
            }
        });

        //Default starting point is user location
        getUserLocationPath();

        // track location layer
        enableMyLocation();
    }

    /**
     * Enables location tracking on the map
     */
    private void enableMyLocation() {
        if (!gMapController.toggleLocationTracking(true)) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Gets the User Location and invokes drawPath
     */
    private void getUserLocationPath() {
        if(fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            //If you are emulating and need to change your current location, use the emulator controls.
                            startingPoint = new LatLng(location.getLatitude(), location.getLongitude());
                            hasUserLocationBeenSet = true;
                            setStartingPoint(true, "", 0f, 0f);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Gets the address as a String given coordinates
     * @param latitude double
     * @param longitude double
     * @return String
     */
    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);  // Return the first address line
            } else {
                return "Address not found";
            }
        } catch (IOException e) {
            Log.e("getAddressFromLocation()", e.getMessage());
            return "Unable to fetch address";
        }
    }

    //Note: It would be best if this was handled by the map class
    /**
     * This handles the calls to the map to create the route
     * @param origin LatLng
     * @param destination LatLng
     */
    private void drawPath(LatLng origin, LatLng destination) {
        //In case someone changes their starting location before their destination
        if(!isDestinationSet) {
            return;
        }

        gMapController.clearPolyLines();
        gMapController.clearAllMarkers();
        gMapController.addMarker(origin, "Current location", BitmapDescriptorFactory.HUE_AZURE);

        gMapController.addMarker(destination, "Destination");

        gMapController.centerOnCoordinates(origin.latitude,origin.longitude);

        new FetchPathTask(this).fetchRoute(origin, destination, travelMode);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    //Note: It would be best if this is handled by the map class
    /**
     * Will add the route to the Map
     * Invoked when the route is fetched by the Google API
     * @param info JSONArray
     */
    @Override
    public void onRouteFetched(JSONArray info) {
        try {
            JSONArray steps = info.getJSONArray(0);
            TextView estimatedTime = findViewById(R.id.estimatedTime);
            String estimatedTimeValue = info.getString(1);
            estimatedTime.setText(getString(R.string.estimated_time, estimatedTimeValue));

            // Handles Route not fetched
            if (steps == null) {
                Toast.makeText(this, "Failed to fetch route", Toast.LENGTH_SHORT).show();
                return;
            }

            gMapController.parseRoutePolylineAndDisplay(steps);
        } catch (JSONException e) {
            Log.e("Route Parsing Error", e.toString());
        }
    }
}



