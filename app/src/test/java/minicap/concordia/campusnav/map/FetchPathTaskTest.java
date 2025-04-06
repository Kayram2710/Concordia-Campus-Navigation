package minicap.concordia.campusnav.map;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.map.FetchPathTask.OnRouteFetchedListener;
import minicap.concordia.campusnav.map.enums.MapColors;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class FetchPathTaskTest {

    // Dummy listener to satisfy constructor; we won't use the asynchronous callbacks in these tests.
    private static class DummyListener implements OnRouteFetchedListener {
        @Override
        public void onRouteFetched(JSONArray steps) {
            // no-op
        }
        @Override
        public void onPlacesFetched(List<OutdoorPOI> outdoorPOIS, MapCoordinates location) {
            // no-op
        }
    }

    private FetchPathTask fetchTask;

    @Before
    public void setUp() {
        fetchTask = new FetchPathTask(new DummyListener());
    }

    @Test
    public void testConvertSecondsToTime_MinutesOnly() {
        String result = fetchTask.convertSecondsToTime("600s");
        // 600 seconds = 10 minutes
        assertEquals("10min", result);
    }

    @Test
    public void testConvertSecondsToTime_Hours() {
        String result = fetchTask.convertSecondsToTime("3600s");
        // 3600 seconds = 60 minutes = 1h0min
        assertEquals("1h0min", result);
    }

    @Test
    public void testParseRoute_Valid() {
        // Build a valid JSON string with one route containing one leg.
        String json = "{ \"routes\": [ { \"legs\": [ { \"duration\": \"600s\", \"steps\": [ { \"dummy\": \"step\" } ] } ] } ] }";
        JSONArray result = fetchTask.parseRoute(json);
        assertNotNull("Expected non-null JSONArray", result);
        // Expecting 2 items: index 0: steps array, index 1: formatted arrival time.
        assertEquals(2, result.length());
        try {
            JSONArray stepsArray = result.getJSONArray(0);
            assertNotNull(stepsArray);
            String arrivalTime = result.getString(1);
            assertEquals("10min", arrivalTime);
        } catch (JSONException e) {
            fail("JSONException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseRoute_InvalidJson() {
        // Provide invalid JSON so that parsing fails and method returns null.
        String invalidJson = "this is not valid json";
        JSONArray result = fetchTask.parseRoute(invalidJson);
        assertNull("Expected null result on invalid JSON", result);
    }

    @Test
    public void testParsePOI_Valid() {
        // Build valid JSON for POI parsing.
        // We'll assume that the POIType used is POIType.RESTAURANT.
        String json = "{ \"places\": [ " +
                "{ \"displayName\": { \"text\": \"Test POI\" }, " +
                "  \"location\": { \"latitude\": 45.0, \"longitude\": -73.0 }, " +
                "  \"accessibilityOptions\": { \"wheelchairAccessible\": true } }," +
                "{ \"location\": { \"latitude\": 46.0, \"longitude\": -74.0 } }," +
                "{ \"displayName\": { \"text\": \"Inaccessible POI\" }, " +
                "  \"location\": { \"latitude\": 47.0, \"longitude\": -75.0 }, " +
                "  \"accessibilityOptions\": { \"wheelchairAccessible\": false } }" +
                "]}";
        // Use an existing POIType (e.g., POIType.RESTAURANT)
        POIType poiType = POIType.RESTAURANT;
        List<OutdoorPOI> placesList = fetchTask.parsePOI(json, poiType);
        assertNotNull(placesList);
        assertEquals(3, placesList.size());

        OutdoorPOI poi1 = placesList.get(0);
        // Use reflection to access the "name" field from the superclass (POI).
        try {
            Field nameField = poi1.getClass().getSuperclass().getDeclaredField("name");
            nameField.setAccessible(true);
            String nameValue = (String) nameField.get(poi1);
            assertEquals("Test POI", nameValue);
        } catch (Exception e) {
            fail("Reflection error: " + e.getMessage());
        }

        OutdoorPOI poi2 = placesList.get(1);
        try {
            Field nameField = poi2.getClass().getSuperclass().getDeclaredField("name");
            nameField.setAccessible(true);
            String nameValue = (String) nameField.get(poi2);
            // When displayName is missing, the code uses "Unknown Place"
            assertEquals("Unknown Place", nameValue);
        } catch (Exception e) {
            fail("Reflection error: " + e.getMessage());
        }

        OutdoorPOI poi3 = placesList.get(2);
        try {
            Field nameField = poi3.getClass().getSuperclass().getDeclaredField("name");
            nameField.setAccessible(true);
            String nameValue = (String) nameField.get(poi3);
            assertEquals("Inaccessible POI", nameValue);
        } catch (Exception e) {
            fail("Reflection error: " + e.getMessage());
        }
    }

    @Test
    public void testParsePOI_InvalidJson() {
        String invalidJson = "not a valid json";
        POIType poiType = POIType.RESTAURANT;
        List<OutdoorPOI> placesList = fetchTask.parsePOI(invalidJson, poiType);
        assertNotNull(placesList);
        // On exception, the method logs and returns an empty list.
        assertEquals(0, placesList.size());
    }
}
