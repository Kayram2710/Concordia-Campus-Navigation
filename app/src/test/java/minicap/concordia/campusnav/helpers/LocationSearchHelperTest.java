package minicap.concordia.campusnav.helpers;

import static org.junit.Assert.*;

import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocationSearchHelperTest {

    private MockWebServer mockWebServer;

    @Before
    public void setUp() throws Exception {
        // Start the mock web server to simulate HTTP responses.
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @After
    public void tearDown() throws Exception {
        // Shutdown the server after tests.
        mockWebServer.shutdown();
    }

    @Test
    public void testParseLocationResults() throws Exception {
        String jsonResponse = "{ \"predictions\": [" +
                "{\"description\": \"Location A\"}," +
                "{\"description\": \"Location B\"}" +
                "] }";
        List<String> locations = LocationSearchHelper.parseLocationResults(jsonResponse);
        assertNotNull("Parsed locations should not be null", locations);
        assertEquals("There should be 2 locations", 2, locations.size());
        assertEquals("Location A", locations.get(0));
        assertEquals("Location B", locations.get(1));
    }

    @Test
    public void testFetchLocationResults() throws Exception {
        // Prepare a fake JSON response.
        String jsonResponse = "{ \"predictions\": [" +
                "{\"description\": \"Fake Location\"}" +
                "] }";
        // Enqueue a successful HTTP response.
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse));

        // Get the URL from the mock server.
        String urlString = mockWebServer.url("/").toString();
        List<String> locations = LocationSearchHelper.fetchLocationResults(urlString);
        assertNotNull("Locations list should not be null", locations);
        assertEquals("There should be 1 location", 1, locations.size());
        assertEquals("Fake Location", locations.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchLocationWithNullQuery() throws Exception {
        LocationSearchHelper.searchLocation(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchLocationWithEmptyQuery() throws Exception {
        LocationSearchHelper.searchLocation("");
    }

}
