// File: app/src/test/java/minicap/concordia/campusnav/map/enums/SupportedMapsTest.java
package minicap.concordia.campusnav.map.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Local unit tests for the {@link SupportedMaps} enum.
 * Placed in src/test/java to be run as a JVM test (Robolectric/JUnit).
 */
public class SupportedMapsTest {

    @Test
    public void testValues() {
        SupportedMaps[] values = SupportedMaps.values();
        // Basic assertions to ensure both constants are present.
        assertNotNull("Enum values should not be null", values);
        assertEquals("There should be exactly 2 enum constants", 2, values.length);
        assertEquals("First enum constant should be GOOGLE_MAPS",
                SupportedMaps.GOOGLE_MAPS, values[0]);
        assertEquals("Second enum constant should be MAPPED_IN",
                SupportedMaps.MAPPED_IN, values[1]);
    }

    @Test
    public void testValueOf_googleMaps() {
        SupportedMaps result = SupportedMaps.valueOf("GOOGLE_MAPS");
        assertEquals("Should match the GOOGLE_MAPS enum constant",
                SupportedMaps.GOOGLE_MAPS, result);
    }

    @Test
    public void testValueOf_mappedIn() {
        SupportedMaps result = SupportedMaps.valueOf("MAPPED_IN");
        assertEquals("Should match the MAPPED_IN enum constant",
                SupportedMaps.MAPPED_IN, result);
    }
}
