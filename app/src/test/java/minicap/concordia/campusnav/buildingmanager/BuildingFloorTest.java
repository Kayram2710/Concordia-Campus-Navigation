package minicap.concordia.campusnav.buildingmanager;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;

import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import org.junit.Test;

public class BuildingFloorTest {

    @Test
    public void testInitialValues() {
        String floorName = "1";
        BuildingName building = BuildingName.HALL;
        String floorPlanResource = "floor_plan.png";

        BuildingFloor floor = new BuildingFloor(floorName, building, floorPlanResource);

        assertEquals("Floor name should match", floorName, floor.getFloorName());
        assertEquals("Associated building should match", building, floor.getAssociatedBuilding());
        assertEquals("Floor plan resource should match", floorPlanResource, floor.getFloorPlanResource());
        assertTrue("Initial list of POIs should be empty", floor.getAllPOIsForFloor().isEmpty());
    }

    @Test
    public void testPOIFiltering() throws Exception {
        // Create a BuildingFloor instance.
        BuildingFloor floor = new BuildingFloor("2", BuildingName.HALL, "resource.png");

        // Use reflection to access the private floorPOIs field.
        Field field = BuildingFloor.class.getDeclaredField("floorPOIs");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<IndoorPOI> poiList = (List<IndoorPOI>) field.get(floor);

        // Create two IndoorPOIs.
        IndoorPOI classroom = new IndoorPOI("Class 101", POIType.CLASS_ROOM, BuildingName.HALL, "2", false, 45.0f, -73.0f);
        IndoorPOI elevator = new IndoorPOI("Elevator A", POIType.ELEVATOR, BuildingName.HALL, "2", true, 45.1f, -73.1f);

        // Manually add them to the floor's POI list.
        poiList.add(classroom);
        poiList.add(elevator);

        // Test getAllPOIsForFloor returns 2 POIs.
        List<IndoorPOI> allPOIs = floor.getAllPOIsForFloor();
        assertEquals("Total POIs should be 2", 2, allPOIs.size());

        // Test filtering by POI type.
        List<IndoorPOI> classrooms = floor.getPOIsOfType(POIType.CLASS_ROOM);
        assertEquals("Should return one classroom", 1, classrooms.size());
        assertEquals("Classroom name should match", "Class 101", classrooms.get(0).getPoiName());

        // Test accessibility filtering.
        List<IndoorPOI> accessibilityPOIs = floor.getAccessibilityPOIs();
        assertEquals("Should return one accessibility POI", 1, accessibilityPOIs.size());
        assertEquals("Accessibility POI should be Elevator A", "Elevator A", accessibilityPOIs.get(0).getPoiName());
    }
}
