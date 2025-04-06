package minicap.concordia.campusnav.map.enums;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapColorsTest {

    @Test
    public void testEnumValues() {
        // Verify the enum contains exactly two constants
        MapColors[] values = MapColors.values();
        assertEquals("MapColors should have 2 values", 2, values.length);

        // Check that the expected constants are present
        assertEquals("First enum constant should be DEFAULT", MapColors.DEFAULT, values[0]);
        assertEquals("Second enum constant should be BLUE", MapColors.BLUE, values[1]);
    }

    @Test
    public void testEnumValueOf() {
        // Test that valueOf returns the correct enum constant.
        assertEquals("Should return DEFAULT", MapColors.DEFAULT, MapColors.valueOf("DEFAULT"));
        assertEquals("Should return BLUE", MapColors.BLUE, MapColors.valueOf("BLUE"));
    }

    @Test
    public void testEnumName() {
        // Verify that the enum's name() method returns the expected string.
        assertEquals("DEFAULT", MapColors.DEFAULT.name());
        assertEquals("BLUE", MapColors.BLUE.name());
    }
}
