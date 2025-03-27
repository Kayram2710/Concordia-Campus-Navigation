package minicap.concordia.campusnav.components;

import java.util.List;

// Holds shuttle schedule data for a day/campus
public class ShuttleSchedule {
    private String day; // Day range (e.g. "Monday-Thursday")
    private String campus; // Campus name (Loyola/SGW)
    private List<String> departureTimes; // List of departure times

    // Constructor and getters
    public ShuttleSchedule(String day, String campus, List<String> departureTimes) {
        this.day = day;
        this.campus = campus;
        this.departureTimes = departureTimes;
    }

    public String getDay() {
        return day;
    }

    public String getCampus() {
        return campus;
    }

    public List<String> getDepartureTimes() {
        return departureTimes;
    }
}
