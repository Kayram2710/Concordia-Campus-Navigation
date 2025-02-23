package minicap.concordia.campusnav.screens;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.beans.LoyolaBuildingCoordinates;
import minicap.concordia.campusnav.beans.SGWBuildingCoordinates;
import minicap.concordia.campusnav.beans.SGWBuildingCoordinates;
import minicap.concordia.campusnav.databinding.ActivityMapsBinding;
import minicap.concordia.campusnav.helpers.CoordinateResHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String KEY_STARTING_LAT = "starting_lat";
    public static final String KEY_STARTING_LNG = "starting_lng";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private double startingLat;
    private double startingLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            startingLat = bundle.getDouble(KEY_STARTING_LAT);
            startingLng = bundle.getDouble(KEY_STARTING_LNG);
        }

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        LatLng concordia = new LatLng(startingLat, startingLng);
        mMap.addMarker(new MarkerOptions().position(concordia).title("Marker at Concordia"));

        float defaultZoom = CoordinateResHelper.getFloat(this, R.dimen.default_map_zoom);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(concordia, defaultZoom));

        // create building shapes
        for (PolygonOptions polygonOptions : SGWBuildingCoordinates.getBuildingCoordinates()){
            mMap.addPolygon(polygonOptions);
        }

        for (PolygonOptions polygonOptions : LoyolaBuildingCoordinates.getBuildingCoordinates()){
            mMap.addPolygon(polygonOptions);
        }

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