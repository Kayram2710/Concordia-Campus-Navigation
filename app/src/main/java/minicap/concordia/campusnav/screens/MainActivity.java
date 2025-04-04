package minicap.concordia.campusnav.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import minicap.concordia.campusnav.R;

import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.map.MapCoordinates;
import minicap.concordia.campusnav.savedstates.States;

public class MainActivity extends AppCompatActivity {

    private ConcordiaBuildingManager buildingManager;
    private Switch switchDarkMode;
    private SharedPreferences sharedPreferences;

    private final States states = States.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildingManager = ConcordiaBuildingManager.getInstance();
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

        switchDarkMode = findViewById(R.id.switch_darkmode);

        // 🔹 Check current system-wide theme
        boolean isSystemDarkMode = isSystemInDarkMode();
        boolean isAppDarkMode = sharedPreferences.getBoolean("DarkMode", isSystemDarkMode);

        // 🔹 Set switch state
        switchDarkMode.setChecked(isAppDarkMode);

        // 🔹 Update theme when switch is toggled
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> setDarkMode(isChecked));

        // 🔹 Listen for system theme changes
        getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onResume(@NonNull LifecycleOwner owner) {
                boolean newSystemDarkMode = isSystemInDarkMode();
                boolean savedDarkMode = sharedPreferences.getBoolean("DarkMode", newSystemDarkMode);
                switchDarkMode.setChecked(savedDarkMode);
            }
        });


        subscribeButtons(this);
    }

    private void setDarkMode(boolean enableDarkMode) {
        if (enableDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        sharedPreferences.edit().putBoolean("DarkMode", enableDarkMode).apply();
    }

    private boolean isSystemInDarkMode() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
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