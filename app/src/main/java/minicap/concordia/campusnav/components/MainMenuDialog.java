package minicap.concordia.campusnav.components;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.sidesheet.SideSheetDialog;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.savedstates.States;
import minicap.concordia.campusnav.screens.ClassScheduleActivity;
import minicap.concordia.campusnav.screens.MapsActivity;

public class MainMenuDialog extends SideSheetDialog {

    View slidingMenu;
    ImageButton closeMenuButton;
    ImageButton classScheduleRedirect;
    ImageButton directionsRedirect;
    ImageButton campusMapRedirect;
    ImageButton busScheduleRedirect;
    Context context;
    private SwitchCompat darkModeSwitch;
    private final States states = States.getInstance();

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
        states.toggleMenu(true);
    }


    private void initializeViews(View view) {
        slidingMenu = view.findViewById(R.id.sliding_menu);
        closeMenuButton = view.findViewById(R.id.closeMenu);
        classScheduleRedirect = view.findViewById(R.id.classScheduleRedirect);
        directionsRedirect = view.findViewById(R.id.directionsRedirect);
        campusMapRedirect = view.findViewById(R.id.campusMapRedirect);
        busScheduleRedirect = view.findViewById(R.id.busScheduleRedirect);
        darkModeSwitch = view.findViewById(R.id.switch_darkmode);
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

        darkModeSwitch.setChecked(states.isDarkModeOn());

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (states.isDarkModeOn() != isChecked) {
                states.toggleDarkMode();
                AppCompatDelegate.setDefaultNightMode(isChecked
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO);
                // Recreate the activity to apply the theme change
                if (context instanceof AppCompatActivity) {
                    ((AppCompatActivity) context).recreate();
                }
            }
        });

        // Sync if changed on main screen
        states.addDarkModeChangeListener(isDark -> {
            if (darkModeSwitch.isChecked() != isDark) {
                darkModeSwitch.setChecked(isDark);
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
