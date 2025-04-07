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

    /**
     * Gets the day for this schedule
     * @return String version of the day
     */
    public String getDay() {
        return day;
    }

    /**
     * Gets the campus name associated with this schedule
     * @return String campus name
     */
    public String getCampus() {
        return campus;
    }

    /**
     * Gets the departure times for this schedule
     * @return List of String departure times
     */
    public List<String> getDepartureTimes() {
        return departureTimes;
    }
}
