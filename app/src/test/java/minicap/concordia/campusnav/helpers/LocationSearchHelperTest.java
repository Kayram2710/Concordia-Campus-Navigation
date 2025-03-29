package minicap.concordia.campusnav.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.List;

import minicap.concordia.campusnav.helpers.LocationSearchHelper;

public class LocationSearchHelperTest {

    @Test(expected = IllegalArgumentException.class)
    public void testSearchLocation_nullQuery_throwsException() throws Exception {
        LocationSearchHelper.searchLocation(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchLocation_emptyQuery_throwsException() throws Exception {
        LocationSearchHelper.searchLocation("");
    }

    @Test
    public void testParseLocationResults_validJson_returnsList() throws Exception {
        // A mock JSON response from Google Places Autocomplete
        String jsonResponse = "{\n" +
                "  \"predictions\": [\n" +
                "    { \"description\": \"Location A\" },\n" +
                "    { \"description\": \"Location B\" }\n" +
                "  ]\n" +
                "}";

        List<String> results = LocationSearchHelper.parseLocationResults(jsonResponse);

        // Expect 2 results
        assertEquals(2, results.size());
        assertEquals("Location A", results.get(0));
        assertEquals("Location B", results.get(1));
    }

    @Test
    public void testParseLocationResults_emptyPredictions_returnsEmptyList() throws Exception {
        String jsonResponse = "{ \"predictions\": [] }";

        List<String> results = LocationSearchHelper.parseLocationResults(jsonResponse);

        assertTrue("Should return empty list when no predictions", results.isEmpty());
    }

    @Test
    public void testParseLocationResults_malformedJson_throwsException() {
        // Missing closing brace or invalid format
        String invalidJson = "{ \"predictions\": [ { \"description\": \"Location A\" }";

        try {
            LocationSearchHelper.parseLocationResults(invalidJson);
            fail("Expected an Exception due to malformed JSON");
        } catch (Exception e) {
            // We expect a JSON parse error (org.json.JSONException, etc.)
            assertTrue(e instanceof org.json.JSONException);
        }
    }
}
