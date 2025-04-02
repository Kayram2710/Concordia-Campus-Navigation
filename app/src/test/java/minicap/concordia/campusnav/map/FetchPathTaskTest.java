package minicap.concordia.campusnav.map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import java.security.Security;
import org.conscrypt.Conscrypt;


/**
 * Unit test that calls the *real* fetchRoute(...) method in FetchPathTask,
 * using Mockito inline to mock out the network (URL, HttpURLConnection).
 */
@RunWith(RobolectricTestRunner.class)
public class FetchPathTaskTest {

    private AutoCloseable closeable; // For MockitoAnnotations if needed

    // A test listener that captures the onRouteFetched result
    private static class TestListener implements FetchPathTask.OnRouteFetchedListener {
        JSONArray result;
        CountDownLatch latch = new CountDownLatch(1);

        @Override
        public void onRouteFetched(JSONArray steps) {
            result = steps;
            latch.countDown();
        }
    }

    static {
        Security.insertProviderAt(Conscrypt.newProvider(), 1);
    }

    @Before
    public void setUp() {
        // Initialize any @Mock fields if you have them
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testFetchRoute_realMethodWithMockedNetwork() throws Exception {
        // 1) Prepare a fake JSON response (like the real Google Routes might return)
        String fakeJson = "{\n" +
                "  \"routes\": [\n" +
                "    {\n" +
                "      \"legs\": [\n" +
                "        {\n" +
                "          \"duration\": \"3600s\",\n" +
                "          \"steps\": [\n" +
                "            { \"instruction\": \"Step 1\"},\n" +
                "            { \"instruction\": \"Step 2\"}\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Convert it into an InputStream to simulate HttpURLConnection response
        InputStream fakeStream = new ByteArrayInputStream(fakeJson.getBytes());

        // 2) Mock URL and HttpURLConnection using mockito-inline
        try (
                // Mock static calls to new URL(...)
                MockedStatic<URL> urlMockedStatic = mockStatic(URL.class);
                // Mock constructing HttpURLConnection
                MockedConstruction<HttpURLConnection> connectionConstruction =
                        mockConstruction(HttpURLConnection.class, (mockConn, context) -> {
                            // For any method call on HttpURLConnection, define behavior
                            // Return a ByteArrayOutputStream for getOutputStream()
                            when(mockConn.getOutputStream()).thenReturn(new ByteArrayOutputStream());
                            // Return our fake JSON input stream for getInputStream()
                            when(mockConn.getInputStream()).thenReturn(fakeStream);
                            // A 200 status code
                            when(mockConn.getResponseCode()).thenReturn(200);
                        })
        ) {
            // A dummy URL object. Whenever new URL(...) is called, we return this instance.
            URL dummyUrl = new URL("http://fake.com");
            urlMockedStatic.when(() -> new URL(anyString())).thenReturn(dummyUrl);

            // 3) Create a real FetchPathTask with a real OnRouteFetchedListener
            TestListener listener = new TestListener();
            FetchPathTask task = new FetchPathTask(listener);

            // 4) Call the real fetchRoute(...) method
            LatLng origin = new LatLng(0, 0);
            LatLng destination = new LatLng(1, 1);
            task.fetchRoute(origin, destination, "DRIVE");

            // 5) Wait for the async callback
            boolean completed = listener.latch.await(3, TimeUnit.SECONDS);
            assertTrue("fetchRoute callback should be called within timeout", completed);

            // 6) Verify the result
            assertNotNull("Result should not be null", listener.result);
            try {
                // The first element is the steps array
                // The second element is the converted time (3600s -> "1h0min")
                assertEquals(2, listener.result.length());
                assertEquals("1h0min", listener.result.getString(1));

                // The steps array should have 2 items
                JSONArray steps = listener.result.getJSONArray(0);
                assertEquals(2, steps.length());
            } catch (JSONException e) {
                fail("JSONException: " + e.getMessage());
            }
        }
    }
}
