package minicap.concordia.campusnav.buildingmanager;

import static org.junit.Assert.*;

import static java.util.Map.entry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.map.MapCoordinates;

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
                new MapCoordinates(45.49701, -73.57877, "Hall building"),
                "The Henry F. Hall Building is a high-density hub, located on De Maisonneuve Boulevard, on Concordia’s downtown Sir-George-Williams Campus.\nThe cube-like structure was completed in 1966. Its exterior is made of pre-fabricated, stressed concrete, a feature of the brutalist movement, often associated with French architect Le Corbusier.",
                CampusName.SGW,
                new HashMap<String, BuildingFloor>(Map.ofEntries(
                        entry("1", new BuildingFloor("1", BuildingName.HALL, "m_c456d3c72998c98c")),
                        entry("2", new BuildingFloor("2", BuildingName.HALL, "m_a64b0271f6702bf1")),
                        entry("3", new BuildingFloor("3", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                        entry("4", new BuildingFloor("4", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                        entry("5", new BuildingFloor("5", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                        entry("6", new BuildingFloor("6", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                        entry("7", new BuildingFloor("7", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                        entry("8", new BuildingFloor("8", BuildingName.HALL, "m_60db7dff71a44370")),
                        entry("9", new BuildingFloor("9", BuildingName.HALL, "m_20a3f55cfa5df04e")),
                        entry("10", new BuildingFloor("10", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                        entry("11", new BuildingFloor("11", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID))
                )),
                R.drawable.sgw_h,
                "1455 De Maisonneuve Blvd W, Montreal, QC H3G 1M8",
                BuildingName.HALL,
                "67df02d0aa7c59000baf8d83"
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
        Campus dummyCampus = new Campus(  new MapCoordinates(45.49701, -73.57877, "Sir George William campus"), new ArrayList<>(Arrays.asList(BuildingName.HALL))) {
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
        OutdoorPOI poi1 = new OutdoorPOI(new MapCoordinates(0,0,"POI1"), POIType.WASHROOM, true) {
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
        OutdoorPOI poi2 = new OutdoorPOI(new MapCoordinates(0,0,"POI2"), POIType.ELEVATOR, false) {
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
        OutdoorPOI poi3 = new OutdoorPOI(new MapCoordinates(0,0,"POI3"), POIType.WASHROOM, false) {
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
        List<Building> buildingsForCampus = manager.getBuildingsForCampus(CampusName.SGW);
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
    public void testGetAllBuildings() {
        List<Building> allBuildings = manager.getAllBuildings();
        assertEquals("There should be 1 building in total", 1, allBuildings.size());
        assertEquals("The building should be 'Hall'", "Hall", allBuildings.get(0).getBuildingName());
    }
}
