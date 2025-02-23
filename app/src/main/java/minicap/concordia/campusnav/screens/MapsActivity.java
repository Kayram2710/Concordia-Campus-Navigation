package minicap.concordia.campusnav.screens;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.databinding.ActivityMapsBinding;
import minicap.concordia.campusnav.helpers.CoordinateResHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String KEY_STARTING_LAT = "starting_lat";
    public static final String KEY_STARTING_LNG = "starting_lng";
    public static final String KEY_CAMPUS_NOT_SELECTED = "campus_not_selected";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private final LatLng SGW_LOCATION = new LatLng(45.49701, -73.57877);
    private final LatLng LOY_LOCATION = new LatLng(45.45863, -73.64188);

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private double startingLat;
    private double startingLng;

    private boolean showSGW;

    private Marker campusMarker;

    private Button campusSwitchBtn;

    private TextView campusTextView;

    private String campusNotSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            startingLat = bundle.getDouble(KEY_STARTING_LAT);
            startingLng = bundle.getDouble(KEY_STARTING_LNG);
            campusNotSelected = bundle.getString(KEY_CAMPUS_NOT_SELECTED);
            showSGW = bundle.getBoolean("SHOW_SGW");
        }

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
    }

    private void toggleCampus() {
        // Flipping the building state
        showSGW = !showSGW;
        // Reloading MapsActivity with the new building coordinates
        Intent i = new Intent(MapsActivity.this, MapsActivity.class);
        i.putExtra(KEY_STARTING_LAT, getNewLatitude());
        i.putExtra(KEY_STARTING_LNG, getNewLongitude());
        i.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, showSGW ? "SGW" : "LOY");
        i.putExtra("SHOW_SGW", showSGW); // Maintaining the toggle state here
        startActivity(i);
        finish();
    }

    private double getNewLatitude() {
        return showSGW ? 45.45863: 45.49701;
    }
    private double getNewLongitude() {
        return showSGW ? -73.64188: -73.57877;
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

    private void initializeMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng campusLocation = new LatLng(startingLat, startingLng);

        campusMarker = mMap.addMarker(new MarkerOptions().position(campusLocation).title("Current Campus"));

        float defaultZoom = CoordinateResHelper.getFloat(this, R.dimen.default_map_zoom);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campusLocation, defaultZoom));

        // track location layer
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true); // layer
            mMap.getUiSettings().setMyLocationButtonEnabled(true); // button
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }
}