package minicap.concordia.campusnav.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import minicap.concordia.campusnav.R;

//to be removed
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.components.BuildingInfoBottomSheetFragment;

import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;

public class MainActivity extends AppCompatActivity {

    private ConcordiaBuildingManager buildingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildingManager = ConcordiaBuildingManager.getInstance();

        subscribeButtons(this);
    }

    protected void subscribeButtons(Context appContext) {

        Button sgwCampusBtn = (Button)findViewById(R.id.viewSGWCampusButton);

        sgwCampusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coords = null;

                Campus sgwCampus = buildingManager.getCampus(CampusName.SGW);
                float[] campusCoordinates = sgwCampus.getLocation();
                coords = new LatLng(campusCoordinates[0], campusCoordinates[1]);

                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                i.putExtra(MapsActivity.KEY_STARTING_LAT, coords.latitude);
                i.putExtra(MapsActivity.KEY_STARTING_LNG, coords.longitude);
                i.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, "LOY");
                i.putExtra(MapsActivity.KEY_SHOW_SGW, true);
                startActivity(i);            }
        });

        Button loyCampusBtn = (Button)findViewById(R.id.viewLoyCampusButton);

        loyCampusBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LatLng coords = null;


                Campus loyolaCampus = buildingManager.getCampus(CampusName.LOYOLA);
                float[] campusCoordinates = loyolaCampus.getLocation();
                coords = new LatLng(campusCoordinates[0], campusCoordinates[1]);

                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                i.putExtra(MapsActivity.KEY_STARTING_LAT, coords.latitude);
                i.putExtra(MapsActivity.KEY_STARTING_LNG, coords.longitude);
                i.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, "SGW");
                i.putExtra(MapsActivity.KEY_SHOW_SGW, false);
                startActivity(i);
            }
        });
    }
}