package minicap.concordia.campusnav.buildingmanager.entities.poi;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

public class POITest {

    private DummyPOI dummyPOI;

    // Dummy subclass to allow instantiation since POI's constructor is protected.
    private static class DummyPOI extends POI {
        public DummyPOI(String name, POIType type, boolean isAccessibilityFeature, double latitude, double longitude) {
            super(name, type, isAccessibilityFeature, latitude, longitude);
        }
    }

    @Before
    public void setUp() {
        // dummy POI instance with test values.
        dummyPOI = new DummyPOI("Test POI", POIType.RESTAURANT, true, 45.0, -73.0);
    }

    @Test
    public void testGetPoiName() {
        assertEquals("Test POI", dummyPOI.getPoiName());
    }

    @Test
    public void testGetPOIType() {
        assertEquals(POIType.RESTAURANT, dummyPOI.getPOIType());
    }

    @Test
    public void testGetIsAccessibilityFeature() {
        assertTrue(dummyPOI.getIsAccessibilityFeature());
    }

    @Test
    public void testLocationValues() {
        assertEquals(45.0, dummyPOI.getLatitude(), 0.001);
        assertEquals(-73.0, dummyPOI.getLongitude(), 0.001);
    }
}
