package minicap.concordia.campusnav.components;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;

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
        populateButtons();
        states.toggleMenu(true);
    }

    /**
     * Initializes this menu's UI elements
     * @param view The view used to initialize the private variables
     */
    private void initializeViews(View view) {
        slidingMenu = view.findViewById(R.id.sliding_menu);
        closeMenuButton = view.findViewById(R.id.closeMenu);
        classScheduleRedirect = view.findViewById(R.id.classScheduleRedirect);
        directionsRedirect = view.findViewById(R.id.directionsRedirect);
        campusMapRedirect = view.findViewById(R.id.campusMapRedirect);
        busScheduleRedirect = view.findViewById(R.id.busScheduleRedirect);
        switchDarkMode = view.findViewById(R.id.switch_darkmodeMainMenu);
    }

    /**
     * Initializes buttons and sets listeners
     */
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

        switchDarkMode.setChecked(states.isDarkModeOn());
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            states.toggleDarkMode(isChecked);
        });
    }

    /**
     * Creates an intent for launching the MapsActivity
     * @return Intent for launching MapsActivity
     */
    public Intent campusMapRoutine(){
        states.toggleMenu(false);
        return new Intent(context, MapsActivity.class);
    }

    /**
     * Opens the MapsActivity and then opens the bus schedule on that page
     */
    public void openBusSchedule(){
        Intent intent = campusMapRoutine();

        // Pass an extra to signal that a specific function should run.
        intent.putExtra("OPEN_BUS", true);
        context.startActivity(intent);
    }

    /**
     * Opens the MapsActivity and then opens the Directions menu on launch
     */
    public void openDirections(){
        Intent intent = campusMapRoutine();

        // Pass an extra to signal that a specific function should run.
        intent.putExtra("OPEN_DIR", true);
        context.startActivity(intent);
    }

    /**
     * Opens the ClassScheduleActivity
     */
    public void openClassSchedule() {
        states.toggleMenu(false);
        Intent intent = new Intent(context, ClassScheduleActivity.class);
        context.startActivity(intent);
    }

    /**
     * Opens the MapsActivity
     */
    public void openCampusMap(){
        Intent intent = campusMapRoutine();
        context.startActivity(intent);
    }

    /**
     * Closes the menu
     */
    public void close(){
        cancel();
    }

    @Override
    public void onStop() {
        super.onStop();
        states.toggleMenu(false);
    }

}
