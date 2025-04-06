package minicap.concordia.campusnav.buildingmanager.enumerations;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class POITypeTest {

    @Test
    public void testGetValue() {
        assertEquals("washroom", POIType.WASHROOM.getValue());
        assertEquals("water_fountain", POIType.WATER_FOUNTAIN.getValue());
        assertEquals("elevator", POIType.ELEVATOR.getValue());
        assertEquals("restaurant", POIType.RESTAURANT.getValue());
        // Note: The COFFEE_SHOP constant is mapped to "cafe" per your code.
        assertEquals("cafe", POIType.COFFEE_SHOP.getValue());
        assertEquals("classroom", POIType.CLASS_ROOM.getValue());
    }
}
