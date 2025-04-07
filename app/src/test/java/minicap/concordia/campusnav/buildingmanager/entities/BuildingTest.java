package minicap.concordia.campusnav.buildingmanager.entities;

import static org.junit.Assert.*;

import static java.util.Map.entry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.map.MapCoordinates;

public class BuildingTest {

    private Building building;
    private BuildingFloor floor1;
    private BuildingFloor floor2;
    private HashMap<String, BuildingFloor> floors;

    @Before
    public void setUp() {
        floor1 = new BuildingFloor("First Floor", BuildingName.HALL, "floorPlan1");
        floor2 = new BuildingFloor("Second Floor", BuildingName.HALL, "floorPlan2");

        floors = new HashMap<>();
        floors.put("1st", floor1);
        floors.put("2nd", floor2);

        building = new Building(
                new MapCoordinates(45.49701, -73.57877, "Hall building"),
                "A test building",
                CampusName.SGW,
                floors,
                R.drawable.sgw_h,
                "1455 De Maisonneuve Blvd W, Montreal, QC H3G 1M8",
                BuildingName.HALL,
                "67df02d0aa7c59000baf8d83"
        );
    }

    @Test
    public void testGetBuildingIdentifier() {
        assertEquals("Expected HALL as building identifier",
                BuildingName.HALL, building.getBuildingIdentifier());
    }

    @Test
    public void testGetBuildingAddress() {
        assertEquals("1455 De Maisonneuve Blvd W, Montreal, QC H3G 1M8", building.getBuildingAddress());
    }

    @Test
    public void testGetBuildingImageRes() {
        assertEquals(2131231075, building.getBuildingImageRes());
    }

    @Test
    public void testGetBuildingName() {
        assertEquals("Hall building", building.getBuildingName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("A test building", building.getDescription());
    }

    @Test
    public void testGetFloors() {
        Collection<BuildingFloor> floorCollection = building.getFloors();
        assertNotNull(floorCollection);
        assertEquals("There should be 2 floors", 2, floorCollection.size());
        assertTrue(floorCollection.contains(floor1));
        assertTrue(floorCollection.contains(floor2));
    }

    @Test
    public void testGetFloor() {
        assertEquals("First Floor object expected", floor1, building.getFloor("1st"));
        assertEquals("Second Floor object expected", floor2, building.getFloor("2nd"));
        assertNull("Non-existent floor should return null", building.getFloor("3rd"));
    }

    @Test
    public void testGetAssociatedCampus() {
        assertEquals("SGW", building.getAssociatedCampus().toString());
    }

    @Test
    public void testToString() {
        assertEquals("Hall building", building.toString());
    }
}
