package minicap.concordia.campusnav.buildingshape;

import static org.junit.Assert.*;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.Enumeration;

import org.junit.Before;
import org.junit.Test;

public class LoyolaCoordinatesResource_en_CATest {

    private LoyolaCoordinatesResource_en_CA resource;

    @Before
    public void setUp() {
        resource = new LoyolaCoordinatesResource_en_CA();
    }

    @Test
    public void testGetContentsNotEmpty() {
        Object[][] contents = resource.getContents();
        assertNotNull("Contents should not be null", contents);
        assertTrue("Contents should not be empty", contents.length > 0);
    }

    @Test
    public void testAdBuildingEntry() {
        Object adBuildingObj = resource.getObject("adBuilding");
        assertNotNull("adBuilding entry should not be null", adBuildingObj);
        assertTrue("adBuilding should be an instance of PolygonOptions", adBuildingObj instanceof PolygonOptions);
        PolygonOptions adBuilding = (PolygonOptions) adBuildingObj;
        // Verify stroke and fill colors
        assertEquals("Stroke color should be 0xFF912338", 0xFF912338, adBuilding.getStrokeColor());
        assertEquals("Fill color should be 0xFF912338", 0xFF912338, adBuilding.getFillColor());
        // Verify that points were added (the polygon should have at least one point)
        assertNotNull("Points list should not be null", adBuilding.getPoints());
        assertFalse("Points list should not be empty", adBuilding.getPoints().isEmpty());
    }

    @Test
    public void testCjBuildingEntry() {
        Object cjBuildingObj = resource.getObject("cjBuilding");
        assertNotNull("cjBuilding entry should not be null", cjBuildingObj);
        assertTrue("cjBuilding should be an instance of PolygonOptions", cjBuildingObj instanceof PolygonOptions);
        PolygonOptions cjBuilding = (PolygonOptions) cjBuildingObj;
        // For "cjBuilding", we check at least the stroke color.
        assertEquals("cjBuilding stroke color should be 0xFF912338", 0xFF912338, cjBuilding.getStrokeColor());
        // The fillColor is not set in the "cjBuilding" entry so it may be 0 by default.
    }

    @Test
    public void testKeysCount() {
        // Verify that the resource bundle returns the expected number of keys.
        // Based on the implementation, the following keys are defined:
        // "adBuilding", "bbAnnex", "bhAnnex", "Loyola Central Building", "cjBuilding", "doDome",
        // "fcBuilding", "geBuilding", "haBuilding", "hbBuilding", "hcBuilding", "huBuilding",
        // "jrResidence", "pcBuilding", "psBuilding", "Vanier Library/Extension Building",
        // "pyBuilding", "raBuilding", "rfBuilding", "siBuilding", "spBuilding", "taBuilding"
        // That is 22 keys in total.
        Enumeration<String> keys = resource.getKeys();
        assertNotNull("Keys enumeration should not be null", keys);
        int count = 0;
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            assertNotNull("Key should not be null", key);
            count++;
        }
        assertEquals("There should be 22 keys in the resource bundle", 22, count);
    }

    @Test
    public void testOtherEntriesHavePoints() {
        // Check a few entries to ensure their PolygonOptions have points.
        String[] keysToTest = {"bbAnnex", "bhAnnex", "Loyola Central Building"};
        for (String key : keysToTest) {
            Object obj = resource.getObject(key);
            assertNotNull(key + " should not be null", obj);
            assertTrue(key + " should be a PolygonOptions instance", obj instanceof PolygonOptions);
            PolygonOptions poly = (PolygonOptions) obj;
            assertNotNull(key + " points should not be null", poly.getPoints());
            assertFalse(key + " points should not be empty", poly.getPoints().isEmpty());
        }
    }
}
