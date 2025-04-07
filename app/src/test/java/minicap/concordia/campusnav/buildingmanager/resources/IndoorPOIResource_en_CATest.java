package minicap.concordia.campusnav.buildingmanager.resources;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

public class IndoorPOIResource_en_CATest {

    private IndoorPOIResource_en_CA resourceBundle;
    private Object[][] contents;

    @Before
    public void setUp() {
        // Instantiate the resource bundle.
        resourceBundle = new IndoorPOIResource_en_CA();
        contents = resourceBundle.getContents();
    }

    @Test
    public void testContentsSize() {
        // Count the expected number of entries.
        // According to the resource bundle:
        //  - HALL floors: Floor1 to Floor11 = 11 entries
        //  - MOLSON_SCHOOL_OF_BUSINESS: FloorS2, FloorS1, Floor1 to Floor15 = 2 + 15 = 17 entries
        //  - LOYOLA_CENTRAL_BUILDING: 1 entry (Floor1)
        //  - VANIER_LIBRARY: Floor1, Floor2, Floor3 = 3 entries
        //  - VANIER_EXTENSION: Floor1, Floor2, Floor3 = 3 entries
        // Total expected = 11 + 17 + 1 + 3 + 3 = 35 entries
        assertNotNull("Contents should not be null", contents);
        assertEquals("Contents should have 35 entries", 35, contents.length);
    }

    @Test
    public void testHallFloor1Entry() {
        String expectedKey = BuildingName.HALL.getResourceName() + "_Floor1";
        boolean found = false;
        for (Object[] entry : contents) {
            if (expectedKey.equals(entry[0])) {
                found = true;
                // For HALL_Floor1, the value is created with 3 IndoorPOI objects.
                assertTrue("Value should be an ArrayList", entry[1] instanceof ArrayList);
                ArrayList<?> list = (ArrayList<?>) entry[1];
                assertEquals("HALL Floor1 should have 74 IndoorPOIs", 74, list.size());
                break;
            }
        }
        assertTrue("Expected key " + expectedKey + " was not found", found);
    }

    @Test
    public void testHallFloor3EntryIsEmpty() {
        String expectedKey = BuildingName.HALL.getResourceName() + "_Floor3";
        boolean found = false;
        for (Object[] entry : contents) {
            if (expectedKey.equals(entry[0])) {
                found = true;
                assertTrue("HALL Floor3 value should be an ArrayList", entry[1] instanceof ArrayList);
                ArrayList<?> list = (ArrayList<?>) entry[1];
                assertEquals("HALL Floor3 should have 0 IndoorPOIs", 0, list.size());
                break;
            }
        }
        assertTrue("Expected key " + expectedKey + " was not found", found);
    }

    @Test
    public void testMolsonFloorS2EntryIsNotEmpty() {
        String expectedKey = BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_FloorS2";
        boolean found = false;
        for (Object[] entry : contents) {
            if (expectedKey.equals(entry[0])) {
                found = true;
                assertTrue("MOLSON FloorS2 value should be an ArrayList", entry[1] instanceof ArrayList);
                ArrayList<?> list = (ArrayList<?>) entry[1];
                assertEquals("MOLSON FloorS2 should have 2 IndoorPOIs", 2, list.size());
                break;
            }
        }
        assertTrue("Expected key " + expectedKey + " was not found", found);
    }

    @Test
    public void testLoyolaCentralBuildingFloor1EntryIsNotEmpty() {
        String expectedKey = BuildingName.LOYOLA_CENTRAL_BUILDING.getResourceName() + "_Floor1";
        boolean found = false;
        for (Object[] entry : contents) {
            if (expectedKey.equals(entry[0])) {
                found = true;
                assertTrue("Loyola Central Building Floor1 value should be an ArrayList", entry[1] instanceof ArrayList);
                ArrayList<?> list = (ArrayList<?>) entry[1];
                assertEquals("Loyola Central Building Floor1 should have 2 IndoorPOIs", 2, list.size());
                break;
            }
        }
        assertTrue("Expected key " + expectedKey + " was not found", found);
    }
}
