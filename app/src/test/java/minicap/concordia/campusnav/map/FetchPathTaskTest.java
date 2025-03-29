package minicap.concordia.campusnav.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import minicap.concordia.campusnav.map.FetchPathTask;
import minicap.concordia.campusnav.map.FetchPathTask.OnRouteFetchedListener;

@RunWith(RobolectricTestRunner.class)
public class FetchPathTaskTest {

    /**
     * A test subclass that overrides the network call to simulate a fake JSON response.
     */
    private static class TestFetchPathTask extends FetchPathTask {
        private final OnRouteFetchedListener testListener;

        public TestFetchPathTask(OnRouteFetchedListener listener) {
            super(listener);
            this.testListener = listener;
        }

        @Override
        public void fetchRoute(LatLng originObj, LatLng destinationObj, String travelModeStr) {
            // Simulate a fake JSON response from the API:
            String fakeJson = "{ \"routes\": [ { \"legs\": [ { \"duration\": \"3600s\", " +
                    "\"steps\": [ {\"instruction\": \"Step 1\"}, {\"instruction\": \"Step 2\"} ] } ] } ] }";
            JSONArray parsed = parseRoute(fakeJson);
            // Directly call the listener (simulate immediate main thread post)
            testListener.onRouteFetched(parsed);
        }
    }

    /**
     * Test that parseRoute correctly parses a valid JSON response.
     * In this case, a JSON with a single leg having duration "180s" (which should convert to "3min")
     * and one step.
     */
    @Test
    public void testParseRoute_validJson_returnsExpectedArray() throws JSONException {
        String fakeJson = "{ \"routes\": [ { \"legs\": [ { \"duration\": \"180s\", " +
                "\"steps\": [ {\"instruction\": \"Step A\"} ] } ] } ] }";
        // We use an anonymous subclass that doesn't perform fetchRoute.
        FetchPathTask task = new FetchPathTask(null) {
            @Override
            public void fetchRoute(LatLng originObj, LatLng destinationObj, String travelModeStr) {
                // Not used in this test.
            }
        };
        JSONArray result = task.parseRoute(fakeJson);
        assertNotNull("Parsed route should not be null", result);
        // Expect two elements: first is the steps array, second is the time string.
        assertEquals("Expected two elements in parsed result", 2, result.length());
        // Verify steps array has one element.
        JSONArray steps = result.getJSONArray(0);
        assertEquals("Steps array should contain one step", 1, steps.length());
        // Verify time conversion: "180s" -> "3min"
        String duration = result.getString(1);
        assertEquals("3min", duration);
    }

    /**
     * Test that fetchRoute (overridden) calls the listener with the expected parsed route.
     */
    @Test
    public void testFetchRoute_callsListenerWithParsedRoute() {
        final JSONArray[] callbackResult = new JSONArray[1];

        OnRouteFetchedListener listener = new OnRouteFetchedListener() {
            @Override
            public void onRouteFetched(JSONArray steps) {
                callbackResult[0] = steps;
            }
        };

        TestFetchPathTask task = new TestFetchPathTask(listener);
        // Provide dummy origin and destination.
        LatLng origin = new LatLng(0, 0);
        LatLng destination = new LatLng(1, 1);
        task.fetchRoute(origin, destination, "driving");

        // Since our overridden fetchRoute is synchronous, the listener should have been called.
        assertNotNull("Listener should be called with a non-null JSONArray", callbackResult[0]);
        // According to our fake JSON in TestFetchPathTask, duration "3600s" converts to "1h0min"
        assertEquals("Parsed route should have two elements", 2, callbackResult[0].length());
        JSONArray steps = callbackResult[0].optJSONArray(0);
        assertNotNull("Steps array should not be null", steps);
        assertEquals("There should be two steps", 2, steps.length());
        String duration = callbackResult[0].optString(1, null);
        assertEquals("1h0min", duration);
    }

    /**
     * Test that parseRoute returns null for malformed JSON.
     */
    @Test
    public void testParseRoute_malformedJson_returnsNull() {
        String invalidJson = "{ \"routes\": [ { \"legs\": [ { \"duration\": \"3600s\", " +
                "\"steps\": [ {\"instruction\": \"Step 1\"} ] } ] } "; // missing closing braces
        FetchPathTask task = new FetchPathTask(null) {
            @Override
            public void fetchRoute(LatLng originObj, LatLng destinationObj, String travelModeStr) {
                // Not used in this test.
            }
        };
        JSONArray result = task.parseRoute(invalidJson);
        assertNull("For malformed JSON, parseRoute should return null", result);
    }
}
