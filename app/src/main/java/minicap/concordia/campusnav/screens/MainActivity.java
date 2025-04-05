package minicap.concordia.campusnav.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import minicap.concordia.campusnav.R;

import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.map.MapCoordinates;
import minicap.concordia.campusnav.savedstates.States;

public class MainActivity extends AppCompatActivity {

    private ConcordiaBuildingManager buildingManager;

    private final States states = States.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildingManager = ConcordiaBuildingManager.getInstance();

        setupDarkModeSwitch();
        subscribeButtons(this);
    }

    private void setupDarkModeSwitch() {
        SwitchCompat darkModeSwitch = findViewById(R.id.switch_darkmode);
        darkModeSwitch.setChecked(states.isDarkModeOn());

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (states.isDarkModeOn() != isChecked) {
                states.toggleDarkMode();
                AppCompatDelegate.setDefaultNightMode(isChecked
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
            }
        });

        // Sync when change happens in main menu dialog
        states.addDarkModeChangeListener(isDark -> {
            if (darkModeSwitch.isChecked() != isDark) {
                darkModeSwitch.setChecked(isDark);
            }
        });
    }

    protected void subscribeButtons(Context appContext) {

        Button sgwCampusBtn = (Button)findViewById(R.id.viewSGWCampusButton);

        sgwCampusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Campus sgwCampus = buildingManager.getCampus(CampusName.SGW);
                states.setCampus(sgwCampus);

                openIntent();
            }
        });

        Button loyCampusBtn = (Button)findViewById(R.id.viewLoyCampusButton);

        loyCampusBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Campus loyolaCampus = buildingManager.getCampus(CampusName.LOYOLA);
                states.setCampus(loyolaCampus);

                openIntent();
            }
        });
    }

    private void openIntent(){
        Campus campus = states.getCampus();
        MapCoordinates campusCoordinates = campus.getLocation();

        Intent i = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(i);
    }
}