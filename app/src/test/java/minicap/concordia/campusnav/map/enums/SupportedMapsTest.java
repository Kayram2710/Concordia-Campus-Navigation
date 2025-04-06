package minicap.concordia.campusnav.map.enums;

import org.junit.Test;
import static org.junit.Assert.*;

public class SupportedMapsTest {

    @Test
    public void testEnumValues() {
        SupportedMaps[] values = SupportedMaps.values();
        assertEquals("SupportedMaps should have 2 values", 2, values.length);
        assertEquals("First enum constant should be GOOGLE_MAPS", SupportedMaps.GOOGLE_MAPS, values[0]);
        assertEquals("Second enum constant should be MAPPED_IN", SupportedMaps.MAPPED_IN, values[1]);
    }

    @Test
    public void testEnumValueOf() {
        assertEquals("Should return GOOGLE_MAPS", SupportedMaps.GOOGLE_MAPS, SupportedMaps.valueOf("GOOGLE_MAPS"));
        assertEquals("Should return MAPPED_IN", SupportedMaps.MAPPED_IN, SupportedMaps.valueOf("MAPPED_IN"));
    }

    @Test
    public void testEnumName() {
        assertEquals("GOOGLE_MAPS", SupportedMaps.GOOGLE_MAPS.name());
        assertEquals("MAPPED_IN", SupportedMaps.MAPPED_IN.name());
    }
}
