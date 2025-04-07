package minicap.concordia.campusnav.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;

import minicap.concordia.campusnav.BuildConfig;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.json.JSONException;
import java.util.concurrent.Executors;


@RunWith(MockitoJUnitRunner.class)
public class GoogleMapsAPITest {

    @Mock
    private FetchPathTask.OnRouteFetchedListener mockListener;

    @Mock
    private HttpURLConnection mockConnection;

    @InjectMocks
    private FetchPathTask fetchPathTask;

    private LatLng testLocation;
    private LatLng destinationLocation;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testLocation = new LatLng(37.7749, -122.4194);
        destinationLocation = new LatLng(40.7128, -74.0060);
        fetchPathTask.executorService = Executors.newSingleThreadExecutor();
    }

    @Test
    public void testParsePOI_validJson() throws JSONException {
        String jsonResponse = "{\"places\":[{\"displayName\":{\"text\":\"Test Place\"},\"location\":{\"latitude\":37.7749,\"longitude\":-122.4194},\"accessibilityOptions\":{\"wheelchairAccessible\":true}}]}";
        List<OutdoorPOI> result = fetchPathTask.parsePOI(jsonResponse, POIType.RESTAURANT);
        assertEquals(1, result.size());
        assertEquals("Test Place", result.get(0).getPoiName());
        assertTrue(result.get(0).getIsAccessibilityFeature());
    }

    @Test
    public void testParsePOI_missingFields() throws JSONException {
        String jsonResponse = "{\"places\":[{\"location\":{\"latitude\":37.7749,\"longitude\":-122.4194}}]}";
        List<OutdoorPOI> result = fetchPathTask.parsePOI(jsonResponse, POIType.RESTAURANT);
        assertEquals(1, result.size());
        assertEquals("Unknown Place", result.get(0).getPoiName());
    }

    @Test
    public void testParsePOI_emptyJson() {
        String jsonResponse = "{}";
        List<OutdoorPOI> result = fetchPathTask.parsePOI(jsonResponse, POIType.RESTAURANT);
        assertEquals(0, result.size());
    }

    @Test
    public void testConvertSecondsToTime_hoursAndMinutes() {
        assertEquals("2h30min", fetchPathTask.convertSecondsToTime("9000s"));
    }

    @Test
    public void testConvertSecondsToTime_onlyMinutes() {
        assertEquals("45min", fetchPathTask.convertSecondsToTime("2700s"));
    }

    @Test
    public void testConvertSecondsToTime_exactHour() {
        assertEquals("1h0min", fetchPathTask.convertSecondsToTime("3600s"));
    }

    @Test
    public void testConvertSecondsToTime_zeroSeconds() {
        assertEquals("0min", fetchPathTask.convertSecondsToTime("0s"));
    }

    @Test
    public void testRoutesAPIConnection() {
        String urlString = "https://routes.googleapis.com/directions/v2:computeRoutes?key=" + BuildConfig.MAPS_API_KEY;

        JsonObject requestBody = new JsonObject();

        JsonObject origin = new JsonObject();
        JsonObject originLocation  = new JsonObject();
        JsonObject originLatLng  = new JsonObject();
        originLatLng.addProperty("latitude", 45.48971252030307);
        originLatLng.addProperty("longitude", -73.58808030276997);
        originLocation.add("latLng", originLatLng);
        origin.add("location", originLocation);
        requestBody.add("origin", origin);

        JsonObject destination = new JsonObject();
        JsonObject destinationLoc = new JsonObject();
        JsonObject destinationLatLng = new JsonObject();
        destinationLatLng.addProperty("latitude", 37.417670);
        destinationLatLng.addProperty("longitude", -122.079595);
        destinationLoc.add("latLng", destinationLatLng);
        destination.add("location", destinationLoc);
        requestBody.add("destination", destination);
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-Goog-FieldMask","*");
            connection.setRequestProperty("X-Goog-Api-Key",BuildConfig.MAPS_API_KEY);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            byte[] input = requestBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);

            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }

            assertEquals("Expected HTTP status 200", 200,connection.getResponseCode());
            assertNotNull("Response body should not be null", responseBody);

            reader.close();
            stream.close();
        }catch (Exception e){
            e.printStackTrace();
            assert false : "API connection failed: " + e.getMessage();
        }

    }

    @Test
    public void testParseRoute_withValidJson() {
        try{
            String jsonResponse = """
                    {
                       "routes": [
                          {
                             "legs": [
                                {
                                   "duration": "503s",
                                   "steps": [
                                      {
                                         "polyline": {
                                            "encodedPolyline": "austGztc`MHIJ?Zj@H^TZJLPDB?LI\\\\{@JQJ@bAbA[`AgA`Dq@xBcCuCsB}B{BcCu@{@QUc@s@eAyAg@}@w@{Aa@y@IOe@_AcBeDOYYe@Ua@KKMM][m@i@aBwA}AsAAA}@s@gA}@e@a@e@a@_@Y_Au@_AaAMMQSYYuAqAcAcAs@s@[[}AyAGEUUt@mB~BkG|@x@`A|@bBvA"
                                         }
                                      }
                                   ]
                                }
                             ]
                          }
                       ]
                    }""";

            FetchPathTask task = new FetchPathTask(null);
            JSONArray steps = task.parseRoute(jsonResponse).getJSONArray(0);
            assertNotNull("The steps should not be null", steps);
            String encodedPolyline = steps.getJSONObject(0).getJSONObject("polyline").getString("encodedPolyline");
            List<LatLng> decodedPoints = PolyUtil.decode(encodedPolyline);
            assertEquals("Latitude should match", 45.4896, decodedPoints.get(0).latitude, 0.0001);
            assertEquals("Longitude should match", -73.5881, decodedPoints.get(0).longitude, 0.0001);
        }catch(Exception e){
            e.printStackTrace();
            assert false : "API connection failed: " + e.getMessage();
        }

    }

    @Test
    public void testParseRoute_withInvalidJson() {
        String invalidJson = "{ invalid json response }";
        FetchPathTask task = new FetchPathTask(null);
        JSONArray steps = task.parseRoute(invalidJson);
        assertNull("The path should be null", steps);
    }
}