package minicap.concordia.campusnav.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.beans.Building;
import minicap.concordia.campusnav.helpers.CoordinateResHelper;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Example on how to fetch object from DB using ID
         * Building u = new Building();
         * u.setId("ID HERE");
         * u.fetch(aVoid -> {
         *    Log.d("MainActivity", "User fetched successfully: " + u.getName());
         *   });
         **/

        /**
         * Example on how to save Object to DB
         * Building u = new Building("test","test","Test");
         * u.save();
         */

        /**
         * Example on how to Fetch object using Field
         * Building u = new Building();
         * u.fetch("name", "test", aVoid -> {
         *    Log.d("MainActivity", "User fetched successfully: " + u.getName());
         * });
         **/

        subscribeButtons(this);
    }

    protected void subscribeButtons(Context appContext) {

        Button sgwCampusBtn = (Button)findViewById(R.id.viewSGWCampusButton);

        sgwCampusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coords = null;
                try {
                    coords = CoordinateResHelper.getCoordsForGoogleMaps(appContext, CoordinateResHelper.BuildingNames.SGWHallBuilding);
                }
                catch (IllegalArgumentException e) {
                    Log.d("SGWCampusButton", "Error while retrieving coordinates for SGW Hall Building");
                }

                if(coords != null) {
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    i.putExtra(MapsActivity.KEY_STARTING_LAT, coords.latitude);
                    i.putExtra(MapsActivity.KEY_STARTING_LNG, coords.longitude);
                    i.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, "LOY");
                    i.putExtra("SHOW_SGW", false);
                    startActivity(i);
                }
            }
        });

        Button loyCampusBtn = (Button)findViewById(R.id.viewLoyCampusButton);

        loyCampusBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LatLng coords = null;
                try {
                    coords = CoordinateResHelper.getCoordsForGoogleMaps(appContext, CoordinateResHelper.BuildingNames.LoyHUBuilding);
                }
                catch (IllegalArgumentException e) {
                    Log.d("LoyCampusBtn", "Error while retrieving coordinates for Loyola HU Building");
                }

                if(coords != null) {
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    i.putExtra(MapsActivity.KEY_STARTING_LAT, coords.latitude);
                    i.putExtra(MapsActivity.KEY_STARTING_LNG, coords.longitude);
                    i.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, "SGW");
                    i.putExtra("SHOW_SGW", true);
                    startActivity(i);
                }
            }
        });
    }
}