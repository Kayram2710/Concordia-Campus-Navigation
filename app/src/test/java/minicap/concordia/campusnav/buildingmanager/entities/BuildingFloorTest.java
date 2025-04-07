package minicap.concordia.campusnav.buildingmanager.entities;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.map.MapCoordinates;

public class BuildingFloorTest {

    private BuildingFloor floor;

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
        IndoorPOI poi1 = new IndoorPOI(new MapCoordinates(0,0,"Washroom"), POIType.WASHROOM, BuildingName.HALL,"first",true);
        IndoorPOI poi2 = new IndoorPOI(new MapCoordinates(0,0,"Cafe"), POIType.COFFEE_SHOP, BuildingName.HALL,"first",true);
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
        IndoorPOI poi1 = new IndoorPOI(new MapCoordinates(0,0,"Resto X"), POIType.RESTAURANT, BuildingName.HALL,"first",true);
        IndoorPOI poi2 = new IndoorPOI(new MapCoordinates(0,0,"Resto Y"), POIType.RESTAURANT, BuildingName.HALL,"first",true);
        IndoorPOI poi3 = new IndoorPOI(new MapCoordinates(0,0,"Coffee Z"), POIType.COFFEE_SHOP, BuildingName.HALL,"first",true);
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
        IndoorPOI poi1 = new IndoorPOI(new MapCoordinates(0,0,"Resto X"), POIType.RESTAURANT, BuildingName.HALL,"first",true);
        IndoorPOI poi2 = new IndoorPOI(new MapCoordinates(0,0,"Resto Y"), POIType.RESTAURANT, BuildingName.HALL,"first",false);
        IndoorPOI poi3 = new IndoorPOI(new MapCoordinates(0,0,"Coffee Z"), POIType.COFFEE_SHOP, BuildingName.HALL,"first",true);
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
