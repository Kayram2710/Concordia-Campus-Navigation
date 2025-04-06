package minicap.concordia.campusnav.buildingmanager.entities;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

public class BuildingFloorTest {

    private BuildingFloor floor;

    // Dummy implementation of IndoorPOI that calls the real parent constructor.
    private static class DummyIndoorPOI extends IndoorPOI {
        public DummyIndoorPOI(String name, POIType type, boolean accessibility) {
            super(
                    name,
                    type,
                    BuildingName.HALL,
                    "TestFloor",
                    accessibility,
                    0.0,
                    0.0
            );
        }
    }

    @Before
    public void setUp() {
        // BuildingFloor requires (floorName, BuildingName, floorPlanResource)
        floor = new BuildingFloor("First Floor", BuildingName.HALL, "floorPlanResource");
    }

    @Test
    public void testGetFloorName() {
        assertEquals("First Floor", floor.getFloorName());
    }

    @Test
    public void testGetFloorPlanResource() {
        assertEquals("floorPlanResource", floor.getFloorPlanResource());
    }

    @Test
    public void testGetAssociatedBuilding() {
        assertEquals(BuildingName.HALL, floor.getAssociatedBuilding());
    }

    @Test
    public void testGetAllPOIsForFloorInitiallyEmpty() {
        assertTrue("POI list should initially be empty", floor.getAllPOIsForFloor().isEmpty());
    }

    @Test
    public void testSetFloorPOIsAndGetAllPOIsForFloor() {
        List<IndoorPOI> poiList = new ArrayList<>();
        DummyIndoorPOI poi1 = new DummyIndoorPOI("Restaurant A", POIType.RESTAURANT, true);
        DummyIndoorPOI poi2 = new DummyIndoorPOI("Coffee B", POIType.COFFEE_SHOP, false);
        poiList.add(poi1);
        poiList.add(poi2);

        floor.setFloorPOIs(poiList);

        List<IndoorPOI> retrievedPOIs = new ArrayList<>(floor.getAllPOIsForFloor());
        assertEquals(2, retrievedPOIs.size());
        assertTrue(retrievedPOIs.contains(poi1));
        assertTrue(retrievedPOIs.contains(poi2));
    }

    @Test
    public void testGetPOIsOfType() {
        List<IndoorPOI> poiList = new ArrayList<>();
        DummyIndoorPOI poi1 = new DummyIndoorPOI("Resto X", POIType.RESTAURANT, true);
        DummyIndoorPOI poi2 = new DummyIndoorPOI("Resto Y", POIType.RESTAURANT, false);
        DummyIndoorPOI poi3 = new DummyIndoorPOI("Coffee Z", POIType.COFFEE_SHOP, true);
        poiList.add(poi1);
        poiList.add(poi2);
        poiList.add(poi3);

        floor.setFloorPOIs(poiList);

        List<IndoorPOI> restaurants = floor.getPOIsOfType(POIType.RESTAURANT);
        assertEquals(2, restaurants.size());
        assertTrue(restaurants.contains(poi1));
        assertTrue(restaurants.contains(poi2));

        List<IndoorPOI> coffees = floor.getPOIsOfType(POIType.COFFEE_SHOP);
        assertEquals(1, coffees.size());
        assertTrue(coffees.contains(poi3));
    }

    @Test
    public void testGetAccessibilityPOIs() {
        List<IndoorPOI> poiList = new ArrayList<>();
        DummyIndoorPOI poi1 = new DummyIndoorPOI("Accessible Resto", POIType.RESTAURANT, true);
        DummyIndoorPOI poi2 = new DummyIndoorPOI("Inaccessible Resto", POIType.RESTAURANT, false);
        DummyIndoorPOI poi3 = new DummyIndoorPOI("Accessible Coffee", POIType.COFFEE_SHOP, true);
        poiList.add(poi1);
        poiList.add(poi2);
        poiList.add(poi3);

        floor.setFloorPOIs(poiList);

        List<IndoorPOI> accessibilityPOIs = floor.getAccessibilityPOIs();
        assertEquals(2, accessibilityPOIs.size());
        assertTrue(accessibilityPOIs.contains(poi1));
        assertTrue(accessibilityPOIs.contains(poi3));
        assertFalse(accessibilityPOIs.contains(poi2));
    }
}
