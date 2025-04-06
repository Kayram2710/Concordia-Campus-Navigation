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

public class ShuttleScraperTest {

    /**
     * Helper method that tries to retrieve a field value from an object using one of the possible field names.
     */
    private Object getFieldValue(Object instance, String... possibleFieldNames) {
        for (String fieldName : possibleFieldNames) {
            Class<?> clazz = instance.getClass();
            while (clazz != null) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field.get(instance);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    clazz = clazz.getSuperclass();
                }
            }
        }
        throw new RuntimeException("None of the fields " + Arrays.toString(possibleFieldNames)
                + " found in instance of " + instance.getClass().getName());
    }

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
            List<?> schedules = ShuttleScraper.fetchSchedule();

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
            Object schedule0 = schedules.get(0);
            String day0 = (String) getFieldValue(schedule0, "day", "mDay");
            String location0 = (String) getFieldValue(schedule0, "location", "mLocation");
            List<?> times0 = (List<?>) getFieldValue(schedule0, "times", "mTimes");
            assertEquals("Monday-Thursday", day0);
            assertEquals("Loyola", location0);
            assertEquals(2, times0.size());
            assertEquals("08:00", times0.get(0));
            assertEquals("09:00", times0.get(1));

            // Schedule 1: Monday-Thursday, SGW
            Object schedule1 = schedules.get(1);
            String day1 = (String) getFieldValue(schedule1, "day", "mDay");
            String location1 = (String) getFieldValue(schedule1, "location", "mLocation");
            List<?> times1 = (List<?>) getFieldValue(schedule1, "times", "mTimes");
            assertEquals("Monday-Thursday", day1);
            assertEquals("SGW", location1);
            assertEquals(2, times1.size());
            assertEquals("08:15", times1.get(0));
            assertEquals("09:15", times1.get(1));

            // Schedule 2: Friday, Loyola
            Object schedule2 = schedules.get(2);
            String day2 = (String) getFieldValue(schedule2, "day", "mDay");
            String location2 = (String) getFieldValue(schedule2, "location", "mLocation");
            List<?> times2 = (List<?>) getFieldValue(schedule2, "times", "mTimes");
            assertEquals("Friday", day2);
            assertEquals("Loyola", location2);
            assertEquals(1, times2.size());
            assertEquals("10:00", times2.get(0));

            // Schedule 3: Friday, SGW
            Object schedule3 = schedules.get(3);
            String day3 = (String) getFieldValue(schedule3, "day", "mDay");
            String location3 = (String) getFieldValue(schedule3, "location", "mLocation");
            List<?> times3 = (List<?>) getFieldValue(schedule3, "times", "mTimes");
            assertEquals("Friday", day3);
            assertEquals("SGW", location3);
            assertEquals(1, times3.size());
            assertEquals("10:15", times3.get(0));
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
