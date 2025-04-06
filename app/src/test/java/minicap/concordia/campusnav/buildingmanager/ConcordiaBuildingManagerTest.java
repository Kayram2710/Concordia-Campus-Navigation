package minicap.concordia.campusnav.buildingmanager;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

public class ConcordiaBuildingManagerTest {

    private ConcordiaBuildingManager manager;

    @Before
    public void setUp() throws Exception {
        // Reset the singleton instance for test isolation.
        Field instanceField = ConcordiaBuildingManager.class.getDeclaredField("mInstance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        // Get a new instance.
        manager = ConcordiaBuildingManager.getInstance();

        // Replace the buildings map with our test data.
        HashMap<BuildingName, Building> buildings = new HashMap<>();
        Building dummyBuilding = new Building(
                "Hall",                // building name
                "Dummy description",   // description
                CampusName.SGW,        // campus name
                new HashMap<String, BuildingFloor>(), // floors (empty for test)
                0.0,                   // latitude
                0.0,                   // longitude
                1,                     // number of floors (dummy value)
                "Dummy address",       // address
                BuildingName.HALL      // building enum value
        ) {
            @Override
            public String getBuildingName() {
                return "Hall";
            }
        };
        buildings.put(BuildingName.HALL, dummyBuilding);

        Field buildingsField = ConcordiaBuildingManager.class.getDeclaredField("buildings");
        buildingsField.setAccessible(true);
        buildingsField.set(manager, buildings);

        // Replace the campuses map with our test data.
        HashMap<CampusName, Campus> campuses = new HashMap<>();
        // Supply dummy latitude and longitude values (0.0, 0.0) as required.
        Campus dummyCampus = new Campus("SGW", new ArrayList<>(Arrays.asList(BuildingName.HALL)), 0.0, 0.0) {
            @Override
            public ArrayList<BuildingName> getAssociatedBuildings() {
                return new ArrayList<>(Arrays.asList(BuildingName.HALL));
            }
        };
        campuses.put(CampusName.SGW, dummyCampus);

        Field campusesField = ConcordiaBuildingManager.class.getDeclaredField("campuses");
        campusesField.setAccessible(true);
        campusesField.set(manager, campuses);

        // Replace the outdoorPOIs list with our test data.
        ArrayList<OutdoorPOI> outdoorPOIs = new ArrayList<>();

        // Create a dummy POI that simulates an accessible POI using POIType.WASHROOM.
        OutdoorPOI poi1 = new OutdoorPOI("POI1", POIType.WASHROOM, true, 0f, 0f) {
            @Override
            public POIType getPOIType() {
                return POIType.WASHROOM;
            }
            @Override
            public boolean getIsAccessibilityFeature() {
                return true;
            }
        };

        // Create a dummy POI with a different type (simulate non-accessible) using POIType.ELEVATOR.
        OutdoorPOI poi2 = new OutdoorPOI("POI2", POIType.ELEVATOR, false, 0f, 0f) {
            @Override
            public POIType getPOIType() {
                return POIType.ELEVATOR;
            }
            @Override
            public boolean getIsAccessibilityFeature() {
                return false;
            }
        };

        // Create a dummy POI with type WASHROOM but with accessibility flag false.
        OutdoorPOI poi3 = new OutdoorPOI("POI3", POIType.WASHROOM, false, 0f, 0f) {
            @Override
            public POIType getPOIType() {
                return POIType.WASHROOM;
            }
            @Override
            public boolean getIsAccessibilityFeature() {
                return false;
            }
        };

        outdoorPOIs.add(poi1);
        outdoorPOIs.add(poi2);
        outdoorPOIs.add(poi3);

        Field outdoorPOIsField = ConcordiaBuildingManager.class.getDeclaredField("outdoorPOIs");
        outdoorPOIsField.setAccessible(true);
        outdoorPOIsField.set(manager, outdoorPOIs);
    }

    @Test
    public void testGetInstance() {
        ConcordiaBuildingManager instance1 = ConcordiaBuildingManager.getInstance();
        ConcordiaBuildingManager instance2 = ConcordiaBuildingManager.getInstance();
        assertSame("Singleton instance should be the same", instance1, instance2);
    }

    @Test
    public void testGetBuilding() {
        Building building = manager.getBuilding(BuildingName.HALL);
        assertNotNull("Building HALL should exist", building);
        assertEquals("Hall", building.getBuildingName());
        assertNull("Non-existent building should return null", manager.getBuilding(BuildingName.MOLSON_SCHOOL_OF_BUSINESS));
    }

    @Test
    public void testGetCampus() {
        Campus campus = manager.getCampus(CampusName.SGW);
        assertNotNull("Campus SGW should exist", campus);
        assertEquals(Arrays.asList(BuildingName.HALL), campus.getAssociatedBuildings());
        assertNull("Non-existent campus should return null", manager.getCampus(CampusName.LOYOLA));
    }

    @Test
    public void testGetBuildingsForCampus() {
        ArrayList<Building> buildingsForCampus = manager.getBuildingsForCampus(CampusName.SGW);
        assertEquals("There should be 1 building for campus SGW", 1, buildingsForCampus.size());
        assertEquals("Hall", buildingsForCampus.get(0).getBuildingName());

        // For a campus not present in our test data, an empty list should be returned.
        assertTrue("Non-existent campus should return an empty list", manager.getBuildingsForCampus(CampusName.LOYOLA).isEmpty());
    }

    @Test
    public void testSearchBuildingsByName() {
        // Test case-insensitive partial match.
        assertEquals("Searching for 'hal' should return one building", 1,
                manager.searchBuildingsByName("hal").size());
        assertEquals("Building name should be 'Hall'",
                "Hall", manager.searchBuildingsByName("hal").get(0).getBuildingName());

        // Search with a string that does not match.
        assertTrue("Searching for 'notfound' should return an empty list",
                manager.searchBuildingsByName("notfound").isEmpty());
    }

    @Test
    public void testGetAllOutdoorPOIs() {
        ArrayList<OutdoorPOI> pois = manager.getAllOutdoorPOIs();
        assertEquals("There should be 3 outdoor POIs", 3, pois.size());
    }

    @Test
    public void testGetAllOutdoorPOIsOfType() {
        // For type WASHROOM, poi1 and poi3 have that type.
        ArrayList<OutdoorPOI> washroomPOIs = manager.getAllOutdoorPOIsOfType(POIType.WASHROOM);
        assertEquals("There should be 2 POIs of type WASHROOM", 2, washroomPOIs.size());

        // For type ELEVATOR, only poi2 should be returned.
        ArrayList<OutdoorPOI> elevatorPOIs = manager.getAllOutdoorPOIsOfType(POIType.ELEVATOR);
        assertEquals("There should be 1 POI of type ELEVATOR", 1, elevatorPOIs.size());
    }

    @Test
    public void testGetOutdoorAccessibilityPOIs() {
        ArrayList<OutdoorPOI> accessibilityPOIs = manager.getOutdoorAccessibilityPOIs();
        // Only poi1 has its accessibility flag set to true.
        assertEquals("There should be 1 accessible outdoor POI", 1, accessibilityPOIs.size());
        // Verify that the returned POI is poi1.
        assertTrue("The accessible POIs should contain poi1",
                accessibilityPOIs.contains(manager.getAllOutdoorPOIs().get(0)));
    }

    @Test
    public void testGetOutdoorAccessibilityPOIsWithType() {
        // For type WASHROOM and accessibility true, only poi1 qualifies.
        ArrayList<OutdoorPOI> result = manager.getOutdoorAccessibilityPOIs(POIType.WASHROOM);
        assertEquals("There should be 1 accessible POI of type WASHROOM", 1, result.size());

        // For type ELEVATOR and accessibility true, none should be returned.
        result = manager.getOutdoorAccessibilityPOIs(POIType.ELEVATOR);
        assertTrue("There should be no accessible POI of type ELEVATOR", result.isEmpty());
    }

    @Test
    public void testGetAllBuildings() {
        ArrayList<Building> allBuildings = manager.getAllBuildings();
        assertEquals("There should be 1 building in total", 1, allBuildings.size());
        assertEquals("The building should be 'Hall'", "Hall", allBuildings.get(0).getBuildingName());
    }
}
