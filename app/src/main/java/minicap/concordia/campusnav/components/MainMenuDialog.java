package minicap.concordia.campusnav.components;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.sidesheet.SideSheetDialog;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.map.MapCoordinates;
import minicap.concordia.campusnav.savedstates.States;
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
    Switch switchDarkMode;
    SharedPreferences sharedPreferences;
    Context context;
    private final States states = States.getInstance();

    public interface MainMenuListener {
    }

    public MainMenuDialog(Context context) {
        super(context);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.main_menu, null);
        setContentView(view);

        //Make main menu start from the left
        setSheetEdge(Gravity.START);

        initializeViews(view);
        setupDarkMode();
        populateButtons();
        states.toggleMenu(true);
    }


    private void initializeViews(View view) {
        slidingMenu = view.findViewById(R.id.sliding_menu);
        closeMenuButton = view.findViewById(R.id.closeMenu);
        classScheduleRedirect = view.findViewById(R.id.classScheduleRedirect);
        directionsRedirect = view.findViewById(R.id.directionsRedirect);
        campusMapRedirect = view.findViewById(R.id.campusMapRedirect);
        busScheduleRedirect = view.findViewById(R.id.busScheduleRedirect);
        switchDarkMode = view.findViewById(R.id.switch_darkmode);
    }

    private void setupDarkMode() {
        boolean isDarkMode = sharedPreferences.getBoolean("DarkMode", false);
        switchDarkMode.setChecked(isDarkMode);

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("DarkMode", isChecked);
                editor.apply();

                AppCompatDelegate.setDefaultNightMode(isChecked
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
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

    public Intent campusMapRoutine(){
        states.toggleMenu(false);
        return new Intent(context, MapsActivity.class);
    }

    public void openBusSchedule(){
        Intent intent = campusMapRoutine();

        // Pass an extra to signal that a specific function should run.
        intent.putExtra("OPEN_BUS", true);
        context.startActivity(intent);
    }

    public void openDirections(){
        Intent intent = campusMapRoutine();

        // Pass an extra to signal that a specific function should run.
        intent.putExtra("OPEN_DIR", true);
        context.startActivity(intent);
    }

    public void openClassSchedule() {
        states.toggleMenu(false);
        Intent intent = new Intent(context, ClassScheduleActivity.class);
        context.startActivity(intent);
    }

    public void openCampusMap(){
        Intent intent = campusMapRoutine();
        context.startActivity(intent);
    }

    public void close(){
        cancel();
    }

    @Override
    public void onStop() {
        super.onStop();
        states.toggleMenu(false);
    }

}
