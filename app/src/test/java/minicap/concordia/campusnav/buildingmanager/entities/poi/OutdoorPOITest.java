package minicap.concordia.campusnav.buildingmanager.entities.poi;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

public class OutdoorPOITest {

    private OutdoorPOI outdoorPOI;

    @Before
    public void setUp() {
        // OutdoorPOI instance with sample data.
        outdoorPOI = new OutdoorPOI("Outdoor Test POI", POIType.RESTAURANT, true, 45.0f, -73.0f);
    }

    @Test
    public void testOutdoorPOIProperties() {
        // getPoiName() because the POI parent class defines it.
        assertEquals("Outdoor Test POI", outdoorPOI.getPoiName());
        assertEquals(POIType.RESTAURANT, outdoorPOI.getPOIType());
        assertTrue(outdoorPOI.getIsAccessibilityFeature());
        assertEquals(45.0f, outdoorPOI.getLatitude(), 0.001);
        assertEquals(-73.0f, outdoorPOI.getLongitude(), 0.001);
    }
}
