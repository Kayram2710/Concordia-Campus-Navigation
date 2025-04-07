package minicap.concordia.campusnav.CalendarService;

import static org.junit.Assert.*;

import org.junit.Test;
import com.google.api.client.util.DateTime;


public class EventItemTest {

    @Test
    public void testGetters() {
        // Arrange: Define test values.
        String title = "Test Event";
        String location = "Test Location";
        long startMillis = 1620000000000L; // example start time
        long endMillis = 1620003600000L;   // example end time (1 hour later)
        DateTime startTime = new DateTime(startMillis);
        DateTime endTime = new DateTime(endMillis);

        // Act: Create an EventItem.
        EventItem event = new EventItem(title, location, startTime, endTime);

        // Assert: Verify getters return the correct values.
        assertEquals("Title should match", title, event.getTitle());
        assertEquals("Location should match", location, event.getLocation());
        assertEquals("Start time should match", startTime, event.getStartTime());
        assertEquals("End time should match", endTime, event.getEndTime());
    }
}
