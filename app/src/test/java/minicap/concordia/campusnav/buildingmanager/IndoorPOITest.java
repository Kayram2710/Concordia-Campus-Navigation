package minicap.concordia.campusnav.buildingmanager;

import static org.junit.Assert.*;

import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import org.junit.Test;

public class IndoorPOITest {

    @Test
    public void testIndoorPOIGetters() {
        String name = "Room A";
        POIType type = POIType.CLASS_ROOM;
        BuildingName building = BuildingName.HALL;
        String floorName = "1";
        boolean accessibility = true;
        float latitude = 45.123f;
        float longitude = -73.456f;

        IndoorPOI indoorPOI = new IndoorPOI(name, type, building, floorName, accessibility, latitude, longitude);

        // Test inherited getters from POI.
        assertEquals("POI name should match", name, indoorPOI.getPoiName());
        assertEquals("POI type should match", type, indoorPOI.getPOIType());
        assertEquals("Accessibility flag should match", accessibility, indoorPOI.getIsAccessibilityFeature());
        // Test IndoorPOI-specific getters.
        assertEquals("Associated building should match", building, indoorPOI.getAssociatedBuilding());
        assertEquals("Floor name should match", floorName, indoorPOI.getFloorName());
    }
}
