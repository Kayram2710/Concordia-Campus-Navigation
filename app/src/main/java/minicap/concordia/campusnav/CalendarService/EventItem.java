package minicap.concordia.campusnav.CalendarService;


import com.google.api.client.util.DateTime;

public class EventItem {
    private final String title;
    private final String location;
    private final DateTime startTime;
    private final DateTime endTime;

    public EventItem(String title, String location, DateTime startTime, DateTime endTime) {
        this.title = title;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public DateTime getStartTime() { return startTime; }
    public DateTime getEndTime() { return endTime; }
}
