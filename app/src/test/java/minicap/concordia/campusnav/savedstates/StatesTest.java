package minicap.concordia.campusnav.savedstates;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.entities.Campus;

public class StatesTest {

    private States states;

    // Dummy subclass of Campus using dummy values for required constructor parameters.
    private static class DummyCampus extends Campus {
        private final String dummyCampusName;

        public DummyCampus(String campusName) {
            // Call the Campus constructor with:
            // - campusName as provided
            // - an empty ArrayList for BuildingName
            // - two dummy double values (0.0, 0.0)
            super(campusName, new ArrayList<>(), 0.0, 0.0);
            this.dummyCampusName = campusName;
        }

        @Override
        public String getCampusName() {
            return dummyCampusName;
        }
    }

    @Before
    public void setUp() {
        // Retrieve the singleton instance of States.
        states = States.getInstance();

        // Reset dark mode and menu state if needed.
        if (states.isDarkModeOn()) {
            states.toggleDarkMode();
        }
        if (states.isMenuOpen()) {
            states.toggleMenu(false);
        }
    }

    @Test
    public void testSingletonInstance() {
        States instance1 = States.getInstance();
        States instance2 = States.getInstance();
        // Both calls should return the same instance.
        assertSame("States instance should be a singleton", instance1, instance2);
    }

    @Test
    public void testToggleDarkMode() {
        // Initially dark mode should be off.
        assertFalse("Dark mode should be off initially", states.isDarkModeOn());

        states.toggleDarkMode();
        assertTrue("Dark mode should be on after toggle", states.isDarkModeOn());

        states.toggleDarkMode();
        assertFalse("Dark mode should be off after second toggle", states.isDarkModeOn());
    }

    @Test
    public void testSetCampusForLoyola() {
        // Use our dummy subclass instead of a Mockito mock.
        Campus campus = new DummyCampus("Loyola campus");

        states.setCampus(campus);

        // Verify that the campus is set.
        assertEquals("Campus should match the dummy campus object", campus, states.getCampus());
        // According to the logic in States,
        // for "Loyola campus", campusName should be "LOYOLA" and other campus should be "SGW".
        assertEquals("LOYOLA", states.getCampusName());
        assertEquals("SGW", states.getOtherCampusName());
        assertEquals("SGW", states.getOtherCampusAbrev());
    }

    @Test
    public void testSetCampusForSGW() {
        Campus campus = new DummyCampus("Sir George William campus");

        states.setCampus(campus);
        assertEquals("Campus should match the dummy campus object", campus, states.getCampus());
        // For "Sir George William campus", campusName should be "SGW",
        // other campus should be "LOYOLA" and abbreviation "LOY".
        assertEquals("SGW", states.getCampusName());
        assertEquals("LOYOLA", states.getOtherCampusName());
        assertEquals("LOY", states.getOtherCampusAbrev());
    }

    @Test
    public void testToggleMenu() {
        // Initially, menuOpen should be false.
        assertFalse("Menu should be closed initially", states.isMenuOpen());
        states.toggleMenu(true);
        assertTrue("Menu should be open after toggle", states.isMenuOpen());
        states.toggleMenu(false);
        assertFalse("Menu should be closed after second toggle", states.isMenuOpen());
    }
}
