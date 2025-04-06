package minicap.concordia.campusnav.buildingmanager.entities.poi;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

public class IndoorPOITest {

    private IndoorPOI poi;

    @Before
    public void setUp() {
        //IndoorPOI instance with test values.
        poi = new IndoorPOI(
                "Test POI",                // name
                POIType.RESTAURANT,        // POI type
                BuildingName.HALL,         // associated building (example)
                "First Floor",             // floor name
                true,                      // accessibility flag
                45.0,                      // latitude
                -73.0                      // longitude
        );
    }

    @Test
    public void testGetAssociatedBuilding() {
        // Verify that getAssociatedBuilding() returns the expected building.
        assertEquals("Associated building should be HALL",
                BuildingName.HALL, poi.getAssociatedBuilding());
    }

    @Test
    public void testGetFloorName() {
        // Verify that getFloorName() returns the correct floor name.
        assertEquals("Floor name should be 'First Floor'",
                "First Floor", poi.getFloorName());
    }

}
