package minicap.concordia.campusnav.screens;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.NonNull;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.widget.EditText;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.buildingshape.CampusBuildingShapes;
import minicap.concordia.campusnav.components.MainMenuDialog;
import minicap.concordia.campusnav.components.placeholder.ShuttleBusScheduleFragment;

import minicap.concordia.campusnav.databinding.ActivityMapsBinding;
import minicap.concordia.campusnav.helpers.UserLocationService;
import minicap.concordia.campusnav.map.FetchPathTask;
import minicap.concordia.campusnav.map.InternalGoogleMaps;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.components.BuildingInfoBottomSheetFragment;
import minicap.concordia.campusnav.map.AbstractMap;
import minicap.concordia.campusnav.map.InternalMappedIn;
import minicap.concordia.campusnav.map.MapCoordinates;
import minicap.concordia.campusnav.map.enums.MapColors;
import minicap.concordia.campusnav.components.BuildingSelectorFragment;
import minicap.concordia.campusnav.map.enums.SupportedMaps;
import minicap.concordia.campusnav.savedstates.States;

public class MapsActivity extends FragmentActivity
        implements AbstractMap.MapUpdateListener, BuildingInfoBottomSheetFragment.BuildingInfoListener, MainMenuDialog.MainMenuListener, UserLocationService.UserLocationUpdatedListener {

    private final String MAPS_ACTIVITY_TAG = "MapsActivity";
    public static final String KEY_STARTING_COORDS = "starting_coords";
    public static final String KEY_CAMPUS_NOT_SELECTED = "campus_not_selected";
    public static final String KEY_SHOW_SGW = "show_sgw";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private AbstractMap map;

    private ConcordiaBuildingManager buildingManager;

    private MapCoordinates startingCoords;

    private boolean isDestinationSet;

    private boolean runBus;
    private boolean runDir;

    private boolean isFirstTimeLoad;

    private boolean isSwitchingMap;

    private Button campusSwitchBtn;

    private TextView campusTextView;

    private EditText yourLocationEditText;

    private EditText destinationEditText;

    TextInputEditText searchText;

    private ImageButton walkButton;
    private ImageButton wheelchairButton;
    private ImageButton carButton;
    private ImageButton transitButton;
    private ImageButton startRouteButton;

    private Button buildingViewButton;
    private int buildingViewButtonMargin;


    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;

    private String travelMode = "DRIVE";

    private MapCoordinates destination;

    private MapCoordinates origin;

    private SupportedMaps currentMap;

    private Fragment curMapFragment;

    private boolean hasUserLocationBeenSet;

    private String eventAddress;

    private final States states = States.getInstance();

    private ConstraintLayout.LayoutParams buildingViewParams;



    // We use this to launch and capture the results of the search location activity
    private ActivityResultLauncher<Intent> searchLocationLauncher;

    private ActivityResultLauncher<Intent> navigationActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        isDestinationSet = false;
        hasUserLocationBeenSet = false;
        isFirstTimeLoad = true;
        isSwitchingMap = false;
        buildingManager = ConcordiaBuildingManager.getInstance();
        currentMap = SupportedMaps.GOOGLE_MAPS;

        //Check bundle for any additional requests
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            runBus = bundle.getBoolean("OPEN_BUS", false);
            runDir = bundle.getBoolean("OPEN_DIR", false);
            eventAddress = bundle.getString("EVENT_ADDRESS", "");
        }

        // Initialize campus map by pulling saved state
        Campus campus = states.getCampus();
        MapCoordinates campusCoordinates = campus.getLocation();
        double startingLat = campusCoordinates.getLat();
        double startingLng = campusCoordinates.getLng();
        startingCoords = new MapCoordinates(startingLat, startingLng);

        // Hook up the Buildings button to show the BuildingSelectorFragment
        buildingViewButton = findViewById(R.id.buildingView);
        buildingViewButton.setOnClickListener(v -> showBuildingSelectorFragment());
        buildingViewParams = (ConstraintLayout.LayoutParams) buildingViewButton.getLayoutParams();
        buildingViewButtonMargin = buildingViewParams.bottomMargin;

        // Shuttle Button to show the Shuttle Bus Schedule
        Button shuttleScheduleView = findViewById(R.id.shuttleScheduleView);
        shuttleScheduleView.setOnClickListener(v -> showShuttleScheduleFragment());

        // Location tracker button setup
        MaterialButton locationButton = findViewById(R.id.locationTracker);
        locationButton.setOnClickListener(v -> centerOnUserLocation());

        // Initialize BottomSheet
        ConstraintLayout bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        int peekHeightPx = (int) (32 * getResources().getDisplayMetrics().density);
        bottomSheetBehavior.setPeekHeight(peekHeightPx);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setFitToContents(true);
        bottomSheetBehavior.setHalfExpandedRatio(0.01f);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        //Adding bottom sheet call back to allow building view button to slide with bottom sheet
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    updateButtonMargin(bottomSheet, 1);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                updateButtonMargin(bottomSheet, slideOffset);
            }
        });

        // Setup campus switching
        campusTextView = findViewById(R.id.ToCampus);
        campusTextView.setText(states.getOtherCampusAbrev());
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
            initializeMap(false);
        }


        // Setup travel mode buttons
        walkButton = findViewById(R.id.walkButton);
        wheelchairButton = findViewById(R.id.wheelchairButton);
        carButton = findViewById(R.id.carButton);
        transitButton = findViewById(R.id.transitButton);
        startRouteButton = findViewById(R.id.startRoute);

        startRouteButton.setOnClickListener(view -> startRoute());

        // Default mode is car
        carButton.setSelected(true);

        walkButton.setOnClickListener(v -> changeSelectedTravelMethod(walkButton, "WALK"));
        wheelchairButton.setOnClickListener(v -> changeSelectedTravelMethod(wheelchairButton, "WHEELCHAIR"));
        carButton.setOnClickListener(v -> changeSelectedTravelMethod(carButton, "DRIVE"));
        transitButton.setOnClickListener(v -> changeSelectedTravelMethod(transitButton, "TRANSIT"));

        // Setup text fields
        yourLocationEditText = findViewById(R.id.yourLocationEditText);

        searchText = findViewById(R.id.genericSearchField);

        ImageView menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> showMainMenuDialog());

        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    launchSearchActivity("", false);
                }
            }
        });

        yourLocationEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                launchSearchActivity(yourLocationEditText.getText().toString(), true);
            }
        });

        destinationEditText = findViewById(R.id.destinationText);
        destinationEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                launchSearchActivity(destinationEditText.getText().toString(), false);
            }
        });

        // Register your activity result launcher
        searchLocationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::HandleSearchLocationResult);

        navigationActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::HandleNavigationActivityResult);

        setStartingPoint(true, "", new MapCoordinates(0, 0));

        if(runBus){
            showShuttleScheduleFragment();
        }

        if(runDir){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheet.post(() -> updateButtonMargin(bottomSheet, 1));
        }

        //Setup POI buttons
        LinearLayout restaurantButton = findViewById(R.id.RestaurantPOI);
        LinearLayout coffeeButton = findViewById(R.id.CoffeePOI);
        LinearLayout fountainButton = findViewById(R.id.FountainPOI);
        LinearLayout elevatorButton = findViewById(R.id.ElevatorPOI);
        LinearLayout washroomButton = findViewById(R.id.WashroomPOI);

        // Set click listeners for each button
        restaurantButton.setOnClickListener(view -> map.displayPOI(origin, POIType.RESTAURANT));
        coffeeButton.setOnClickListener(view -> map.displayPOI(origin, POIType.COFFEE_SHOP));
        //TODO handle Fountain, elevator and washroom (Indoor POI)
    }

    private void updateButtonMargin(View bottomSheet, float slideOffset){
        int progress = (int)(slideOffset * bottomSheet.getHeight());
        int finalProgress = (int)(progress * 0.8f);
        int newMargin = buildingViewButtonMargin + finalProgress;
        buildingViewParams.bottomMargin = Math.max(0, newMargin);
        buildingViewButton.setLayoutParams(buildingViewParams);
    }

    /**
     * Handles Launching NavigationActivity when starting a live route
     */
    public void startRoute() {
        if (origin == null || destination == null) {
            //Error Handling
            return;
        }

        Intent i = new Intent(MapsActivity.this, NavigationActivity.class);
        i.putExtra("origin_lat", origin.getLat());
        i.putExtra("origin_lng", origin.getLng());
        i.putExtra("destination_lat", destination.getLat());
        i.putExtra("destination_lng", destination.getLng());
        i.putExtra("travel_mode", travelMode);

        navigationActivityLauncher.launch(i);

    }

    private void HandleNavigationActivityResult(ActivityResult result) {
        //Empty since no result is being passed
    }

    /**
     * Handles the result of the SearchLocationActivity
     * @param result The result received from the activity
     */
    private void HandleSearchLocationResult(ActivityResult result) {
        runOnUiThread(() -> {
            searchText.clearFocus();
            destinationEditText.clearFocus();
            yourLocationEditText.clearFocus();
        });

        if(result.getResultCode() != LocationSearchActivity.RESULT_OK) {
            Log.d(MAPS_ACTIVITY_TAG, "Result code for search was not 'ok', discarding...");
            return;
        }

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

        boolean isIndoors = returnData.getBoolean(LocationSearchActivity.KEY_RETURN_BOOL_IS_INDOORS);
        boolean useCurrentLocation = returnData.getBoolean(LocationSearchActivity.KEY_RETURN_IS_CURRENT_LOCATION_BOOL);

        if(!useCurrentLocation) {
            if(isIndoors) {
                switchToMap(SupportedMaps.MAPPED_IN);
            }
            else {
                switchToMap(SupportedMaps.GOOGLE_MAPS);
            }
        }
        else {
            //Force the user location to update
            hasUserLocationBeenSet = false;
        }

        boolean isDestination = returnData.getBoolean(LocationSearchActivity.KEY_RETURN_BOOL_IS_DESTINATION);
        String returnedLocation = returnData.getString(LocationSearchActivity.KEY_RETURN_CHOSEN_LOCATION_STRING);
        MapCoordinates newCoords = returnData.getParcelable(LocationSearchActivity.KEY_RETURN_CHOSEN_COORDS, MapCoordinates.class);

        if(isDestination) {
            setDestination(returnedLocation, newCoords);
        }
        else {
            setStartingPoint(useCurrentLocation, returnedLocation, newCoords);
        }

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
     * @param coordinates The coordinates of the starting point
     */
    private void setStartingPoint(boolean useCurrentLocation, String locationString, MapCoordinates coordinates) {
        Log.d(MAPS_ACTIVITY_TAG, "Set starting location to: " + locationString + " with coords: (" + coordinates.getLat() + ", " + coordinates.getLng() + "), is current location: " + useCurrentLocation);

        if(useCurrentLocation && !hasUserLocationBeenSet) {
            UserLocationService.getLastKnownLocation(this, this);
            return;
        }

        String yourLocationText;
        if(useCurrentLocation) {
            yourLocationText = getString(R.string.your_location);
        }
        else {
            hasUserLocationBeenSet = false;
            origin = coordinates;
            yourLocationText = locationString;
        }

        runOnUiThread(() -> {
            yourLocationEditText.setText(yourLocationText);
        });

        if(!isSwitchingMap) {
            drawPath();
        }
    }

    /**
     * Sets the destination used for navigation
     * @param locationString The name of the location
     * @param coordinates The coordinates of the location
     */
    private void setDestination(String locationString, MapCoordinates coordinates) {
        Log.d(MAPS_ACTIVITY_TAG, "Set destination to: " + locationString + " with coords: (" + coordinates.getLat() + ", " + coordinates.getLng() + ")");

        destination = coordinates;
        runOnUiThread(() -> {
            destinationEditText.setText(locationString);
        });
        isDestinationSet = true;

        // We want to ensure that the map is loaded before we draw
        if(!isSwitchingMap) {
            drawPath();
        }
    }

    /**
     * Toggles the map to either the SGW campus or the Loyola campus based on which was already focused
     */
    private void toggleCampus() {
        //get campus from saved states
        String newCampusName = states.getOtherCampusName();

        //converts string "SGW" or "LOYOLA" into the corresponding enum constant
        CampusName wantedCampus = CampusName.valueOf(newCampusName);
        Campus curCampus = buildingManager.getCampus(wantedCampus);

        //save new campus state
        states.setCampus(curCampus);

        MapCoordinates campusCoords = curCampus.getLocation();

        //moving the existing marker to the new campus location
        map.addMarker(campusCoords, curCampus.getCampusName(), true);

        //moving the camera smoothly to the new campus location
        map.centerOnCoordinates(campusCoords);

        //updating the button text
        campusTextView.setText(states.getOtherCampusAbrev());
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

        drawPath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap(false);
            } else {
                Toast.makeText(this,
                        "Location permission is required to show your location",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Switches the current map to a different supported map
     */
    private void switchToMap(SupportedMaps newMap) {
        if(currentMap == newMap) {
            return;
        }

        isSwitchingMap = true;

        currentMap = newMap;

        getSupportFragmentManager().beginTransaction()
                .remove(curMapFragment)
                .commit();

        initializeMap(true);
    }

    /**
     * Initializes the desired map
     */
    private void initializeMap(boolean replace) {
        if(currentMap == SupportedMaps.GOOGLE_MAPS) {
            map = new InternalGoogleMaps(this);
            campusSwitchBtn.setVisibility(VISIBLE);
        }
        else {
            map = new InternalMappedIn(this);
            campusSwitchBtn.setVisibility(GONE);
        }

        curMapFragment = map.initialize();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(replace) {
            ft.replace(R.id.map, curMapFragment);
        }
        else {
            ft.add(R.id.map, curMapFragment);
        }

        ft.commit();
    }

    /**
     * Enables location tracking on the map
     */
    private void enableMyLocation() {
        if (!map.toggleLocationTracking(true)) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
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
                return addresses.get(0).getAddressLine(0);
            } else {
                return "Address not found";
            }
        } catch (IOException e) {
            Log.e("getAddressFromLocation()", e.getMessage());
            return "Unable to fetch address";
        }
    }

    /**
     * This handles the calls to the map to create the route
     */
    private void drawPath() {
        //In case someone changes their starting location before their destination
        if(!isDestinationSet) {
            return;
        }

        map.clearPathFromMap();
        map.clearAllMarkers();
        map.addMarker(origin, "Starting location", MapColors.BLUE);

        map.addMarker(destination, "Destination");

        map.centerOnCoordinates(origin);

        map.displayRoute(origin, destination, travelMode);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady() {
        if(isFirstTimeLoad) {
            isFirstTimeLoad = false;
            map.centerOnCoordinates(startingCoords);
        }

        //By default, origin is user location
        enableMyLocation();

        if(isSwitchingMap) {
            isSwitchingMap = false;
            drawPath();
        }

        // If we got an eventAddress, let's geocode it
        if (eventAddress != null && !eventAddress.isEmpty()) {
            geocodeAndSetDestination(eventAddress);
        }

        //Sets the onclick for POI Markers
        if(currentMap == SupportedMaps.GOOGLE_MAPS){
            ((InternalGoogleMaps)map).getmMap().setOnMarkerClickListener(marker -> {
                if (marker.getTag() != null && "POI".equals(marker.getTag())) {
                    LatLng position = marker.getPosition();
                    double latitude = position.latitude;
                    double longitude = position.longitude;
                    setDestination(marker.getTitle(),new MapCoordinates(latitude,longitude));
                    drawPath();
                }
                return false;
            });
        }
    }
    private void geocodeAndSetDestination(String addressString) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(addressString, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address addr = addresses.get(0);
                double lat = addr.getLatitude();
                double lng = addr.getLongitude();
                setDestination(addressString, new MapCoordinates(lat, lng));
            } else {
                Toast.makeText(this, "Could not find location for: " + addressString, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error geocoding address: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onEstimatedTimeUpdated(String newTime) {
        TextView estimatedTime = findViewById(R.id.estimatedTime);

        estimatedTime.setText(getString(R.string.estimated_time, newTime));
    }

    @Override
    public void onMapError(String errorString) {
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapClicked(MapCoordinates coordinates) {
        String address = getAddressFromLocation(coordinates.getLat(), coordinates.getLng());
        map.addMarker(coordinates, "Clicked location", true);
        setDestination(address, coordinates);
    }

    public void showMainMenuDialog() {
        if(!states.isMenuOpen()) {
            MainMenuDialog dialog = new MainMenuDialog(this);
            dialog.show();
        }
    }


    // Show building selector fragment
    private void showBuildingSelectorFragment() {
        BuildingSelectorFragment fragment = new BuildingSelectorFragment();
        fragment.show(getSupportFragmentManager(), "BuildingSelectorFragment");
    }

    // Show shuttle schedule fragment
    private void showShuttleScheduleFragment(){
        ShuttleBusScheduleFragment shuttleBusScheduleFragment = new ShuttleBusScheduleFragment();
        shuttleBusScheduleFragment.show(getSupportFragmentManager(), "ShuttleBusScheduleFragment");
    }


    @Override
    public void directionButtonOnClick(Building building) {
        MapCoordinates location = building.getLocation();

        setDestination(building.getBuildingName(), location);
    }

    // Replacing default Maps center location button functionality
    private void centerOnUserLocation() {
        if (origin != null) {
            map.centerOnCoordinates(origin);
        } else {
            setStartingPoint(true, "", new MapCoordinates(0, 0));
            Toast.makeText(this, "Getting your location...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnUserLocationUpdated(MapCoordinates newPosition) {
        hasUserLocationBeenSet = true;
        origin = newPosition;
        setStartingPoint(true, "", new MapCoordinates(0, 0));
    }
}
