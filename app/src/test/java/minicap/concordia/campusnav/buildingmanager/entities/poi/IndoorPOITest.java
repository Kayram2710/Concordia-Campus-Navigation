package minicap.concordia.campusnav.buildingmanager.entities.poi;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.map.MapCoordinates;

public class IndoorPOITest {

    private IndoorPOI poi;

    @Before
    public void setUp() {
        //IndoorPOI instance with test values.
        poi = new IndoorPOI(new MapCoordinates(45.49724976, -73.57868389, "H196"), POIType.CLASS_ROOM, BuildingName.HALL, "First Floor", false);

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
