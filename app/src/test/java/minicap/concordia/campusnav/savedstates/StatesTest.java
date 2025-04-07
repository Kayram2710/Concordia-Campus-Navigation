package minicap.concordia.campusnav.savedstates;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.map.MapCoordinates;

public class StatesTest {

    private States states;

    // Dummy subclass of Campus using dummy values for required constructor parameters.
    private static class DummyCampus extends Campus {

        public DummyCampus(MapCoordinates campusCoordinates) {
            // Call the Campus constructor with:
            // - campusName as provided
            // - an empty ArrayList for BuildingName
            // - two dummy double values (0.0, 0.0)
            super(campusCoordinates, new ArrayList<>());
        }

    }

    @Before
    public void setUp() {
        // Retrieve the singleton instance of States.
        states = States.getInstance();

        // Reset dark mode and menu state if needed.
        if (states.isDarkModeOn()) {
            states.toggleDarkMode(false);
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
    public void testToggleDarkMode() throws InterruptedException {
        // Initially dark mode should be off.
        assertFalse("Dark mode should be off initially", states.isDarkModeOn());

        states.toggleDarkMode(true);
        assertTrue("Dark mode should be on after toggle", states.isDarkModeOn());
        Thread.sleep(1010);
        states.toggleDarkMode(false);
        assertFalse("Dark mode should be off after second toggle", states.isDarkModeOn());
    }

    @Test
    public void testSetCampusForLoyola() {
        // Use our dummy subclass instead of a Mockito mock.
        Campus campus =new Campus(
                new MapCoordinates(45.45863, -73.64188, "Loyola campus"),
                new ArrayList<BuildingName>(Arrays.asList(
                        BuildingName.LOYOLA_CENTRAL_BUILDING,
                        BuildingName.ST_IGNATIUS_OF_LOYOLA_CHURCH,
                        BuildingName.CENTRE_FOR_STRUCTURAL_AND_FUNCTIONAL_GENOMICS,
                        BuildingName.PERFORM_CENTRE,
                        BuildingName.HINGSTON_HALL_WING_HC,
                        BuildingName.LOYOLA_JESUIT_HALL_AND_CONFERENCE_CENTRE,
                        BuildingName.RECREATION_AND_ATHLETICS_COMPLEX,
                        BuildingName.QUADRANGLE,
                        BuildingName.TERREBONNE_BUILDING,
                        BuildingName.HINGSTON_HALL_WING_HA,
                        BuildingName.APPLIED_SCIENCE_HUB,
                        BuildingName.OSCAR_PETERSON_CONCERT_HALL,
                        BuildingName.STUDENT_CENTRE,
                        BuildingName.BB_ANNEX,
                        BuildingName.HINGSTON_HALL_WING_HB,
                        BuildingName.RICHARD_J_RENAUD_SCIENCE_COMPLEX,
                        BuildingName.PHYSICAL_SERVICES_BUILDING,
                        BuildingName.SOLAR_HOUSE,
                        BuildingName.VANIER_EXTENSION,
                        BuildingName.VANIER_LIBRARY,
                        BuildingName.JESUIT_RESIDENCE,
                        BuildingName.STINGER_DOME,
                        BuildingName.ADMINISTRATION_BUILDING,
                        BuildingName.F_C_SMITH_BUILDING,
                        BuildingName.BH_ANNEX,
                        BuildingName.COMMUNICATION_STUDIES_AND_JOURNALISM_BUILDING,
                        BuildingName.PSYCHOLOGY_BUILDING
                )));

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
        Campus campus = new Campus(
                new MapCoordinates(45.49701, -73.57877, "Sir George William campus"),
                new ArrayList<BuildingName>(Arrays.asList(
                        BuildingName.CI_ANNEX,
                        BuildingName.GREY_NUNS_ANNEX,
                        BuildingName.HALL,
                        BuildingName.LD_BUILDING,
                        BuildingName.MI_ANNEX,
                        BuildingName.P_ANNEX,
                        BuildingName.FAUBOURG_BUILDING,
                        BuildingName.LEARNING_SQUARE,
                        BuildingName.GUY_DE_MAISONNEUVE_BUILDING,
                        BuildingName.Q_ANNEX,
                        BuildingName.TORONTO_DOMINION_BUILDING,
                        BuildingName.PR_ANNEX,
                        BuildingName.RR_ANNEX,
                        BuildingName.FA_ANNEX,
                        BuildingName.M_ANNEX,
                        BuildingName.MOLSON_SCHOOL_OF_BUSINESS,
                        BuildingName.ER_BUILDING,
                        BuildingName.K_ANNEX,
                        BuildingName.T_ANNEX,
                        BuildingName.CL_ANNEX,
                        BuildingName.R_ANNEX,
                        BuildingName.VISUAL_ARTS_BUILDING,
                        BuildingName.X_ANNEX,
                        BuildingName.B_ANNEX,
                        BuildingName.SAMUEL_BRONFMAN_BUILDING,
                        BuildingName.GS_BUILDING,
                        BuildingName.FAUBOURG_STE_CATHERINE_BUILDING,
                        BuildingName.GREY_NUNS_BUILDING,
                        BuildingName.MU_ANNEX,
                        BuildingName.Z_ANNEX,
                        BuildingName.EN_ANNEX,
                        BuildingName.V_ANNEX,
                        BuildingName.ENGINEERING_COMPUTER_SCIENCE_AND_VISUAL_ARTS_INTEGRATED_COMPLEX,
                        BuildingName.S_ANNEX,
                        BuildingName.D_ANNEX,
                        BuildingName.JW_MCCONNELL_BUILDING
                )));
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
