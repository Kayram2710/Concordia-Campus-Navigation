package minicap.concordia.campusnav.buildingmanager;

import static org.junit.Assert.*;

import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import org.junit.Test;

public class OutdoorPOITest {

    @Test
    public void testOutdoorPOIGetters() {
        String name = "Fountain";
        POIType type = POIType.WATER_FOUNTAIN;
        boolean accessibility = false;
        float latitude = 45.789f;
        float longitude = -73.987f;

        OutdoorPOI outdoorPOI = new OutdoorPOI(name, type, accessibility, latitude, longitude);

        // Test inherited getters from POI.
        assertEquals("POI name should match", name, outdoorPOI.getPoiName());
        assertEquals("POI type should match", type, outdoorPOI.getPOIType());
        assertEquals("Accessibility flag should match", accessibility, outdoorPOI.getIsAccessibilityFeature());
    }
}
