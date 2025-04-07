package minicap.concordia.campusnav.helpers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

import minicap.concordia.campusnav.components.ShuttleSchedule;

public class ShuttleScraperTest {

    @Test
    public void testFetchScheduleSuccess() throws Exception {
        // Create a dummy HTML string with two tables:
        // - First table (Monday-Thursday) with two rows (each row has 2 columns).
        // - Second table (Friday) with one row.
        String html = "<html><body>" +
                "<table>" +
                "<tr><td>08:00</td><td>08:15</td></tr>" +
                "<tr><td>09:00</td><td>09:15</td></tr>" +
                "</table>" +
                "<table>" +
                "<tr><td>10:00</td><td>10:15</td></tr>" +
                "</table>" +
                "</body></html>";
        Document dummyDoc = Jsoup.parse(html);

        // Use Mockito's inline static mocking for Jsoup.
        try (MockedStatic<Jsoup> jsoupMock = Mockito.mockStatic(Jsoup.class)) {
            Connection mockConnection = Mockito.mock(Connection.class);
            jsoupMock.when(() -> Jsoup.connect("https://www.concordia.ca/maps/shuttle-bus.html#depart"))
                    .thenReturn(mockConnection);
            Mockito.when(mockConnection.get()).thenReturn(dummyDoc);

            // Call the method under test.
            List<ShuttleSchedule> schedules = ShuttleScraper.fetchSchedule();

            // Expect 4 ShuttleSchedule objects:
            // Monday-Thursday: Loyola and SGW (two departure times each).
            // Friday: Loyola and SGW (one departure time each).
            assertEquals("Should have 4 schedules", 4, schedules.size());

            // For each schedule, use reflection to retrieve properties.
            // Try several alternatives for each field name.
            // Adjust the alternatives if your class uses different names.
            // For day: try "day", "mDay"
            // For location: try "location", "mLocation"
            // For times: try "times", "mTimes"

            // Schedule 0: Monday-Thursday, Loyola
            ShuttleSchedule schedule0 = schedules.get(0);
            assertEquals("Monday-Thursday", schedule0.getDay());
            assertEquals("Loyola", schedule0.getCampus());
            assertEquals(2, schedule0.getDepartureTimes().size());
            assertEquals("08:00", schedule0.getDepartureTimes().get(0));
            assertEquals("09:00", schedule0.getDepartureTimes().get(1));

            // Schedule 1: Monday-Thursday, SGW
            ShuttleSchedule schedule1 = schedules.get(1);
            assertEquals("Monday-Thursday", schedule1.getDay());
            assertEquals("SGW", schedule1.getCampus());
            assertEquals(2, schedule1.getDepartureTimes().size());
            assertEquals("08:15", schedule1.getDepartureTimes().get(0));
            assertEquals("09:15", schedule1.getDepartureTimes().get(1));

            // Schedule 2: Friday, Loyola
            ShuttleSchedule schedule2 = schedules.get(2);
            assertEquals("Friday", schedule2.getDay());
            assertEquals("Loyola", schedule2.getCampus());
            assertEquals(1, schedule2.getDepartureTimes().size());
            assertEquals("10:00", schedule2.getDepartureTimes().get(0));

            // Schedule 3: Friday, SGW
            ShuttleSchedule schedule3 = schedules.get(3);
            assertEquals("Friday", schedule3.getDay());
            assertEquals("SGW", schedule3.getCampus());
            assertEquals(1, schedule3.getDepartureTimes().size());
            assertEquals("10:15", schedule3.getDepartureTimes().get(0));
        }
    }

    @Test
    public void testFetchScheduleIOException() throws Exception {
        // Simulate an IOException when fetching the HTML.
        try (MockedStatic<Jsoup> jsoupMock = Mockito.mockStatic(Jsoup.class)) {
            Connection mockConnection = Mockito.mock(Connection.class);
            jsoupMock.when(() -> Jsoup.connect("https://www.concordia.ca/maps/shuttle-bus.html#depart"))
                    .thenReturn(mockConnection);
            Mockito.when(mockConnection.get()).thenThrow(new IOException("Test exception"));

            // The method should catch the exception and return an empty list.
            List<?> schedules = ShuttleScraper.fetchSchedule();
            assertNotNull("Schedules list should not be null", schedules);
            assertTrue("Schedules list should be empty on IOException", schedules.isEmpty());
        }
    }
}
