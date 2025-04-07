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

    /**
     * Gets the title of the event item
     * @return String title of event
     */
    public String getTitle() { return title; }

    /**
     * Gets the location of the event item
     * @return String location of event
     */
    public String getLocation() { return location; }

    /**
     * Gets the start time of the event
     * @return DateTime start of the event
     */
    public DateTime getStartTime() { return startTime; }

    /**
     * Gets the end time of the event
     * @return DateTime end of event
     */
    public DateTime getEndTime() { return endTime; }
}
