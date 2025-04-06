package minicap.concordia.campusnav.savedstates;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.entities.Campus;

public class StatesTest {

    private States states;

    // Since States is a singleton, we retrieve its instance.
    @Before
    public void setUp() {
        states = States.getInstance();
        // Reset dark mode and menu state if needed.
        // (Assuming tests run in sequence, we ensure a known starting state.)
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
        // Create a dummy Campus using Mockito.
        Campus campusMock = mock(Campus.class);
        when(campusMock.getCampusName()).thenReturn("Loyola campus");

        states.setCampus(campusMock);
        // Verify that the campus is set.
        assertEquals(campusMock, states.getCampus());
        // According to the logic, for "Loyola campus", campusName should be "LOYOLA"
        // and the other campus (both full name and abbreviation) should be "SGW".
        assertEquals("LOYOLA", states.getCampusName());
        assertEquals("SGW", states.getOtherCampusName());
        assertEquals("SGW", states.getOtherCampusAbrev());
    }

    @Test
    public void testSetCampusForSGW() {
        Campus campusMock = mock(Campus.class);
        when(campusMock.getCampusName()).thenReturn("Sir George William campus");

        states.setCampus(campusMock);
        assertEquals(campusMock, states.getCampus());
        // For "Sir George William campus", campusName should be "SGW",
        // other campus should be "LOYOLA", and abbreviation "LOY".
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
