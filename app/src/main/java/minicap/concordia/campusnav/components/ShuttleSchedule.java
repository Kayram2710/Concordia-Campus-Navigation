package minicap.concordia.campusnav.components;

import java.util.List;

public class ShuttleSchedule {
    private String day;
    private String campus;
    private List<String> departureTimes;

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
