package minicap.concordia.campusnav.buildingshape;

import static org.junit.Assert.*;

import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.Enumeration;

import org.junit.Before;
import org.junit.Test;

public class SGWCoordinatesResource_en_CATest {

    private SGWCoordinatesResource_en_CA resource;

    @Before
    public void setUp() {
        resource = new SGWCoordinatesResource_en_CA();
    }

    @Test
    public void testGetContentsNotEmpty() {
        Object[][] contents = resource.getContents();
        assertNotNull("Contents should not be null", contents);
        assertTrue("Contents should not be empty", contents.length > 0);
    }

    @Test
    public void testBAnnexEntry() {
        Object bAnnexObj = resource.getObject("bAnnex");
        assertNotNull("bAnnex entry should not be null", bAnnexObj);
        assertTrue("bAnnex should be an instance of PolygonOptions", bAnnexObj instanceof PolygonOptions);
        PolygonOptions bAnnex = (PolygonOptions) bAnnexObj;
        // Verify stroke and fill colors
        assertEquals("Stroke color for bAnnex should be 0xFF912338", 0xFF912338, bAnnex.getStrokeColor());
        assertEquals("Fill color for bAnnex should be 0xFF912338", 0xFF912338, bAnnex.getFillColor());
        // Verify that points were added
        assertNotNull("bAnnex points should not be null", bAnnex.getPoints());
        assertFalse("bAnnex points should not be empty", bAnnex.getPoints().isEmpty());
    }

    @Test
    public void testCiAnnexEntry() {
        Object ciAnnexObj = resource.getObject("ciAnnex");
        assertNotNull("ciAnnex entry should not be null", ciAnnexObj);
        assertTrue("ciAnnex should be an instance of PolygonOptions", ciAnnexObj instanceof PolygonOptions);
        PolygonOptions ciAnnex = (PolygonOptions) ciAnnexObj;
        // Check stroke and fill colors
        assertEquals("Stroke color for ciAnnex should be 0xFF912338", 0xFF912338, ciAnnex.getStrokeColor());
        assertEquals("Fill color for ciAnnex should be 0xFF912338", 0xFF912338, ciAnnex.getFillColor());
        // Check that it has points
        assertNotNull("ciAnnex points should not be null", ciAnnex.getPoints());
        assertFalse("ciAnnex points should not be empty", ciAnnex.getPoints().isEmpty());
    }

    @Test
    public void testZAnnexEntry() {
        Object zAnnexObj = resource.getObject("zAnnex");
        assertNotNull("zAnnex entry should not be null", zAnnexObj);
        assertTrue("zAnnex should be an instance of PolygonOptions", zAnnexObj instanceof PolygonOptions);
        PolygonOptions zAnnex = (PolygonOptions) zAnnexObj;
        // Check stroke and fill colors
        assertEquals("Stroke color for zAnnex should be 0xFF912338", 0xFF912338, zAnnex.getStrokeColor());
        assertEquals("Fill color for zAnnex should be 0xFF912338", 0xFF912338, zAnnex.getFillColor());
        // Ensure that points exist
        assertNotNull("zAnnex points should not be null", zAnnex.getPoints());
        assertFalse("zAnnex points should not be empty", zAnnex.getPoints().isEmpty());
    }

    @Test
    public void testKeysCount() {
        Enumeration<String> keys = resource.getKeys();
        assertNotNull("Keys enumeration should not be null", keys);
        int count = 0;
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            assertNotNull("Key should not be null", key);
            count++;
        }
        // There should be exactly 36 keys as defined in the resource bundle.
        assertEquals("There should be 36 keys in the resource bundle", 36, count);
    }
}
