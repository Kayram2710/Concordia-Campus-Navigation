package minicap.concordia.campusnav.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.sidesheet.SideSheetDialog;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.map.MapCoordinates;
import minicap.concordia.campusnav.screens.ClassScheduleActivity;
import minicap.concordia.campusnav.screens.MainActivity;
import minicap.concordia.campusnav.screens.MapsActivity;

public class MainMenuDialog extends SideSheetDialog {

    View slidingMenu;
    ImageButton closeMenuButton;
    ImageButton classScheduleRedirect;
    ImageButton directionsRedirect;
    ImageButton campusMapRedirect;
    ImageButton busScheduleRedirect;
    Context context;

    public interface MainMenuListener {
    }

    public MainMenuDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.main_menu, null);
        setContentView(view);

        //Make main menu start from the left
        setSheetEdge(Gravity.START);

        initializeViews(view);
        populateButtons();
    }


    private void initializeViews(View view) {
        slidingMenu = view.findViewById(R.id.sliding_menu);
        closeMenuButton = view.findViewById(R.id.closeMenu);
        classScheduleRedirect = view.findViewById(R.id.classScheduleRedirect);
        directionsRedirect = view.findViewById(R.id.directionsRedirect);
        campusMapRedirect = view.findViewById(R.id.campusMapRedirect);
        busScheduleRedirect = view.findViewById(R.id.busScheduleRedirect);
    }

    //This passes
    public void populateButtons(){

        closeMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        campusMapRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCampusMap();
            }
        });

        classScheduleRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openClassSchedule();
            }
        });

        busScheduleRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBusSchedule();
            }
        });

        directionsRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDirections();
            }
        });

    }

    public void openBusSchedule(){
        ConcordiaBuildingManager buildingManager = ConcordiaBuildingManager.getInstance();
        Campus sgwCampus = buildingManager.getCampus(CampusName.SGW);
        MapCoordinates campusCoordinates = sgwCampus.getLocation();

        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(MapsActivity.KEY_STARTING_LAT, campusCoordinates.getLat());
        intent.putExtra(MapsActivity.KEY_STARTING_LNG, campusCoordinates.getLng());
        intent.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, "LOY");
        intent.putExtra(MapsActivity.KEY_SHOW_SGW, true);
        // Pass an extra to signal that a specific function should run.
        intent.putExtra("OPEN_BUS", true);
        context.startActivity(intent);
    }

    public void openDirections(){
        ConcordiaBuildingManager buildingManager = ConcordiaBuildingManager.getInstance();
        Campus sgwCampus = buildingManager.getCampus(CampusName.SGW);
        MapCoordinates campusCoordinates = sgwCampus.getLocation();

        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(MapsActivity.KEY_STARTING_LAT, campusCoordinates.getLat());
        intent.putExtra(MapsActivity.KEY_STARTING_LNG, campusCoordinates.getLng());
        intent.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, "LOY");
        intent.putExtra(MapsActivity.KEY_SHOW_SGW, true);
        // Pass an extra to signal that a specific function should run.
        intent.putExtra("OPEN_DIR", true);
        context.startActivity(intent);
    }

    public void openClassSchedule() {
        Intent intent = new Intent(context, ClassScheduleActivity.class);
        context.startActivity(intent);
    }

    public void openCampusMap(){
        ConcordiaBuildingManager buildingManager = ConcordiaBuildingManager.getInstance();
        Campus sgwCampus = buildingManager.getCampus(CampusName.SGW);
        MapCoordinates campusCoordinates = sgwCampus.getLocation();

        Intent i = new Intent(context, MapsActivity.class);
        i.putExtra(MapsActivity.KEY_STARTING_LAT, campusCoordinates.getLat());
        i.putExtra(MapsActivity.KEY_STARTING_LNG, campusCoordinates.getLng());
        i.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, "LOY");
        i.putExtra(MapsActivity.KEY_SHOW_SGW, true);
        context.startActivity(i);
    }

    public void close(){
        cancel();
    }

}
