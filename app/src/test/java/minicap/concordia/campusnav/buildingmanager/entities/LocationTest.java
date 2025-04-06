package minicap.concordia.campusnav.buildingmanager.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.map.MapCoordinates;

public class LocationTest {

    private DummyLocation dummyLocation;

    // Dummy subclass to allow instantiation of the abstract (protected constructor) class.
    private static class DummyLocation extends Location {
        public DummyLocation(double latitude, double longitude) {
            super(latitude, longitude);
        }
    }

    @Before
    public void setUp() {
        dummyLocation = new DummyLocation(45.0, -73.0);
    }

    @Test
    public void testGetLocation() {
        MapCoordinates coords = dummyLocation.getLocation();
        assertNotNull("Coordinates should not be null", coords);
        assertEquals("Latitude should match", 45.0, coords.getLat(), 0.001);
        assertEquals("Longitude should match", -73.0, coords.getLng(), 0.001);
    }

    @Test
    public void testGetLatitude() {
        assertEquals("Latitude should be 45.0", 45.0, dummyLocation.getLatitude(), 0.001);
    }

    @Test
    public void testGetLongitude() {
        assertEquals("Longitude should be -73.0", -73.0, dummyLocation.getLongitude(), 0.001);
    }
}
