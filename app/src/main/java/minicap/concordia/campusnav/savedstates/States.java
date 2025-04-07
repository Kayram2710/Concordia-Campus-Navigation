package minicap.concordia.campusnav.savedstates;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import androidx.appcompat.app.AppCompatDelegate;

import minicap.concordia.campusnav.buildingmanager.entities.Campus;

public class States {

    private Campus campus;

    private String campusName;

    private String otherCampus;

    private String otherCampusAbrev;

    private boolean darkMode = false;

    private boolean menuOpen = false;

    private String selectedCalendarId;

    private static final States instance = new States();

    private long lastToggleTime = 0;
    private static final long DEBOUNCE_DELAY_MS = 1000;

    private States(){}

    /**
     * Gets the current instance of the app state
     * @return The current app state
     */
    public static States getInstance(){
        return instance;
    }

    /**
     * Toggles dark mode in the app
     * @param check Flag for whether dark mode is on or off
     */
    public void toggleDarkMode(boolean check) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastToggleTime < DEBOUNCE_DELAY_MS) {
            return;
        }

        lastToggleTime = currentTime;
        darkMode = check;
        applyDarkMode();
    }

    /**
     * Returns a flag indicating if dark mode is on
     * @return True if dark mode is on, false otherwise
     */
    public boolean isDarkModeOn(){
        return darkMode;
    }

    /**
     * Applies dark mode to the application
     */
    public void applyDarkMode() {
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? MODE_NIGHT_YES
                        : MODE_NIGHT_NO
        );
    }

    /**
     * Gets the campus saved in the app state
     * @return The saved Campus
     */
    public Campus getCampus() {
        return campus;
    }

    /**
     * Sets the Campus that is saved as part of the app state
     * @param campus The campus to save
     */
    public void setCampus(Campus campus) {
        String campusFullName = campus.getCampusName();
        if (campusFullName.equals("Loyola campus")){
            campusName = "LOYOLA";
            otherCampus = "SGW";
            otherCampusAbrev = "SGW";
        } else if (campusFullName.equals("Sir George William campus")){
            campusName = "SGW";
            otherCampus = "LOYOLA";
            otherCampusAbrev = "LOY";
        }
        this.campus = campus;
    }

    /**
     * Gets the name of the saved campus
     * @return String campus name
     */
    public String getCampusName(){
        return campusName;
    }

    /**
     * Gets the name of the campus that is not saved
     * @return String campus name
     */
    public String getOtherCampusName(){
        return otherCampus;
    }

    /**
     * Gets the abbreviated name of the campus that is not saved
     * @return String campus name abbreviated
     */
    public String getOtherCampusAbrev() {
        return otherCampusAbrev;
    }

    /**
     * Returns a flag indicating if the menu is open
     * @return True if the menu is open, false otherwise
     */
    public boolean isMenuOpen() {
        return menuOpen;
    }

    /**
     * Toggles the menu
     * @param state The state of the menu as a boolean
     */
    public void toggleMenu(boolean state){
        menuOpen = state;
    }

    /**
     * Sets the selected calendar's id
     * @param calendarId The current calendar's id
     */
    public void setSelectedCalendarId(String calendarId) {
        this.selectedCalendarId = calendarId;
    }

    /**
     * Gets the current selected calendar ID
     * @return The current calendar ID
     */
    public String getSelectedCalendarId() {
        return this.selectedCalendarId;
    }
}
