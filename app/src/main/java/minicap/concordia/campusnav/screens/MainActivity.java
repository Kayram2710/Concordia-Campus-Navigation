package minicap.concordia.campusnav.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import minicap.concordia.campusnav.R;

import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.savedstates.States;

public class MainActivity extends AppCompatActivity {

    private ConcordiaBuildingManager buildingManager;
    private Switch switchDarkMode;


    private final States states = States.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildingManager = ConcordiaBuildingManager.getInstance();

        switchDarkMode = findViewById(R.id.switch_darkmode);
        switchDarkMode.setChecked(states.isDarkModeOn());
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchDarkMode.setChecked(states.isDarkModeOn());
            states.toggleDarkMode(isChecked);
        });

        subscribeButtons();
    }

    /**
     * Sets on click listeners for the button
     */
    protected void subscribeButtons() {

        Button sgwCampusBtn = findViewById(R.id.viewSGWCampusButton);

        sgwCampusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Campus sgwCampus = buildingManager.getCampus(CampusName.SGW);
                states.setCampus(sgwCampus);

                openIntent();
            }
        });

        Button loyCampusBtn = findViewById(R.id.viewLoyCampusButton);

        loyCampusBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Campus loyolaCampus = buildingManager.getCampus(CampusName.LOYOLA);
                states.setCampus(loyolaCampus);

                openIntent();
            }
        });
    }

    /**
     * Opens the MapsActivity
     */
    private void openIntent(){
        Intent intent = new Intent(this, MapsActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}