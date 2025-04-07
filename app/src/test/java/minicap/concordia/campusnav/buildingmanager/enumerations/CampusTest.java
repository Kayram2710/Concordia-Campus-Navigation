package minicap.concordia.campusnav.buildingmanager.enumerations;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.map.MapCoordinates;

public class CampusTest {

    private Campus campus;
    private ArrayList<BuildingName> buildingNames;

    @Before
    public void setUp() {
        buildingNames = new ArrayList<>(Arrays.asList(BuildingName.HALL, BuildingName.HALL));
        campus = new Campus(new MapCoordinates(45,-73,"Test Campus"), buildingNames);
    }

    @Test
    public void testGetAssociatedBuildings() {
        List<BuildingName> retrieved = campus.getAssociatedBuildings();
        assertNotNull("Associated buildings list should not be null", retrieved);
        assertEquals("There should be 2 associated buildings", 2, retrieved.size());
        // Both entries are HALL.
        assertTrue("List should contain HALL", retrieved.contains(BuildingName.HALL));
    }

    @Test
    public void testGetCampusName() {
        assertEquals("Campus name should be 'Test Campus'", "Test Campus", campus.getCampusName());
    }

    @Test
    public void testLocationValues() {
        assertEquals("Latitude should be 45.0", 45.0, campus.getLatitude(), 0.001);
        assertEquals("Longitude should be -73.0", -73.0, campus.getLongitude(), 0.001);
    }
}
