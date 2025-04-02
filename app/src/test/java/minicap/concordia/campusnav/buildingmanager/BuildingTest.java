package minicap.concordia.campusnav.buildingmanager;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import org.junit.Test;

public class BuildingTest {

    @Test
    public void testGettersAndToString() {
        String buildingName = "Test Building";
        String description = "A test description";
        CampusName campus = CampusName.SGW;
        String buildingAddress = "123 Test St.";
        int imageRes = 101;
        BuildingName identifier = BuildingName.HALL;
        double latitude = 45.0;
        double longitude = -73.0;

        // Create a floor map with one floor.
        HashMap<String, BuildingFloor> floors = new HashMap<>();
        BuildingFloor floor = new BuildingFloor("1", identifier, "floor1.png");
        floors.put("1", floor);

        Building building = new Building(
                buildingName,
                description,
                campus,
                floors,
                latitude,
                longitude,
                imageRes,
                buildingAddress,
                identifier
        );

        // Existing assertions
        assertEquals("Building name should match", buildingName, building.getBuildingName());
        assertEquals("Description should match", description, building.getDescription());
        assertEquals("Campus should match", campus, building.getAssociatedCampus());
        assertEquals("Building address should match", buildingAddress, building.getBuildingAddress());
        assertEquals("Image resource should match", imageRes, building.getBuildingImageRes());

        // **Add an assertion for the building identifier**:
        assertEquals("Building identifier should match", identifier, building.getBuildingIdentifier());

        Collection<BuildingFloor> buildingFloors = building.getFloors();
        assertEquals("There should be one floor", 1, buildingFloors.size());
        assertTrue("Floor should be contained", buildingFloors.contains(floor));

        // toString() should return buildingName.
        assertEquals("toString() should return building name", buildingName, building.toString());
    }
}
