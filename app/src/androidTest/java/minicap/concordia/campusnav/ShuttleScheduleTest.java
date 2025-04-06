package minicap.concordia.campusnav.components;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class ShuttleScheduleTest {

    @Test
    public void testShuttleScheduleConstructorAndGetters() {
        // Arrange
        String expectedDay = "Monday-Thursday";
        String expectedCampus = "SGW";
        List<String> expectedDepartureTimes = Arrays.asList("10:00", "11:00", "12:00");

        // Act
        ShuttleSchedule schedule = new ShuttleSchedule(expectedDay, expectedCampus, expectedDepartureTimes);

        // Assert
        assertEquals("Day should be correct", expectedDay, schedule.getDay());
        assertEquals("Campus should be correct", expectedCampus, schedule.getCampus());
        assertEquals("Departure times should match", expectedDepartureTimes, schedule.getDepartureTimes());
    }
}
