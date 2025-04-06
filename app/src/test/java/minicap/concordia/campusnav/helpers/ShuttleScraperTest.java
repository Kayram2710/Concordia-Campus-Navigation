package minicap.concordia.campusnav.helpers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;


import minicap.concordia.campusnav.components.ShuttleSchedule;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class ShuttleScraperTest {

    private MockedStatic<Jsoup> jsoupMock;

    // Fake HTML containing two tables:
    // Table 1: Monday-Thursday (2 rows)
    // Table 2: Friday (2 rows)
    private final String fakeHtml = "<html><body>" +
            "<table>" +
            "  <tr><td>MT Loy 1</td><td>MT SGW 1</td></tr>" +
            "  <tr><td>MT Loy 2</td><td>MT SGW 2</td></tr>" +
            "</table>" +
            "<table>" +
            "  <tr><td>Fri Loy 1</td><td>Fri SGW 1</td></tr>" +
            "  <tr><td>Fri Loy 2</td><td>Fri SGW 2</td></tr>" +
            "</table>" +
            "</body></html>";

    @Before
    public void setUp() {
        // Begin static mocking for Jsoup.
        jsoupMock = mockStatic(Jsoup.class);
    }

    @After
    public void tearDown() {
        jsoupMock.close();
    }

    @Test
    public void testFetchScheduleParsesHtmlCorrectly() throws Exception {
        // Create a Document from our fake HTML.
        Document fakeDoc = Jsoup.parse(fakeHtml);

        // Create a mock Connection that returns our fake Document.
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.get()).thenReturn(fakeDoc);
        // When any URL is used, return our mockConnection.
        jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);

        // Call fetchSchedule().
        List<ShuttleSchedule> schedules = ShuttleScraper.fetchSchedule();

        // We expect four ShuttleSchedule objects:
        // 0: "Monday-Thursday", "Loyola" with departures ["MT Loy 1", "MT Loy 2"]
        // 1: "Monday-Thursday", "SGW" with departures ["MT SGW 1", "MT SGW 2"]
        // 2: "Friday", "Loyola" with departures ["Fri Loy 1", "Fri Loy 2"]
        // 3: "Friday", "SGW" with departures ["Fri SGW 1", "Fri SGW 2"]
        assertNotNull("Schedules list should not be null", schedules);
        assertEquals("Should return 4 schedules", 4, schedules.size());

        ShuttleSchedule schedule0 = schedules.get(0);
        assertEquals("Monday-Thursday", schedule0.getDay());
        assertEquals("Loyola", schedule0.getCampus());
        assertEquals(Arrays.asList("MT Loy 1", "MT Loy 2"), schedule0.getDepartureTimes());

        ShuttleSchedule schedule1 = schedules.get(1);
        assertEquals("Monday-Thursday", schedule1.getDay());
        assertEquals("SGW", schedule1.getCampus());
        assertEquals(Arrays.asList("MT SGW 1", "MT SGW 2"), schedule1.getDepartureTimes());

        ShuttleSchedule schedule2 = schedules.get(2);
        assertEquals("Friday", schedule2.getDay());
        assertEquals("Loyola", schedule2.getCampus());
        assertEquals(Arrays.asList("Fri Loy 1", "Fri Loy 2"), schedule2.getDepartureTimes());

        ShuttleSchedule schedule3 = schedules.get(3);
        assertEquals("Friday", schedule3.getDay());
        assertEquals("SGW", schedule3.getCampus());
        assertEquals(Arrays.asList("Fri SGW 1", "Fri SGW 2"), schedule3.getDepartureTimes());
    }

    @Test
    public void testFetchScheduleHandlesIOException() throws Exception {
        // Simulate an IOException by having mockConnection.get() throw an exception.
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.get()).thenThrow(new IOException("Simulated error"));
        jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);

        // When an IOException occurs, the method catches it and returns an empty list.
        List<ShuttleSchedule> schedules = ShuttleScraper.fetchSchedule();
        assertNotNull("Schedules list should not be null even on error", schedules);
        assertTrue("Schedules list should be empty when an IOException occurs", schedules.isEmpty());
    }
}
