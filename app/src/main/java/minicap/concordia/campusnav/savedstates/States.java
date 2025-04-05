package minicap.concordia.campusnav.savedstates;

import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.map.MapCoordinates;

public class States {

    private Campus campus;

    private String campusName;

    private String otherCampus;

    private String otherCampusAbrev;

    private boolean darkMode = false;

    private boolean menuOpen = false;

    private String selectedCalendarId;

    private static final States instance = new States();

    private States(){}

    public static States getInstance(){
        return instance;
    }

    public void toggleDarkMode(){
        darkMode = !darkMode;
    }

    public boolean isDarkModeOn(){
        return darkMode;
    }

    public Campus getCampus() {
        return campus;
    }

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

    public String getCampusName(){
        return campusName;
    }

    public String getOtherCampusName(){
        return otherCampus;
    }

    public String getOtherCampusAbrev() {
        return otherCampusAbrev;
    }

    public boolean isMenuOpen() {
        return menuOpen;
    }

    public void toggleMenu(boolean state){
        menuOpen = state;
    }

    public void setSelectedCalendarId(String calendarId) {
        this.selectedCalendarId = calendarId;
    }
    public String getSelectedCalendarId() {
        return this.selectedCalendarId;
    }
}
