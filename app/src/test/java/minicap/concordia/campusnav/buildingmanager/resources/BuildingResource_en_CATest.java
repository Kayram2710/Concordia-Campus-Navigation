package minicap.concordia.campusnav.buildingmanager.resources;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;

public class BuildingResource_en_CATest {

    private BuildingResource_en_CA resource;

    @Before
    public void setUp() {
        resource = new BuildingResource_en_CA();
    }

    @Test
    public void testContentsCount() {
        Object[][] contents = resource.getContents();
        assertEquals(65, contents.length);
    }

    @Test
    public void testCampusSGW() {
        Object obj = resource.getObject(CampusName.SGW.getResourceName());
        assertNotNull("CampusSGW should not be null", obj);
        assertTrue("Object must be a Campus", obj instanceof Campus);
        Campus campus = (Campus) obj;
        assertEquals("Sir George William campus", campus.getCampusName());
        List<BuildingName> expectedBuildings = Arrays.asList(
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
        );
        assertEquals(expectedBuildings, campus.getAssociatedBuildings());
        assertEquals(45.49701, campus.getLatitude(), 0.00001);
        assertEquals(-73.57877, campus.getLongitude(), 0.00001);
    }

    @Test
    public void testCampusLoyola() {
        Object obj = resource.getObject(CampusName.LOYOLA.getResourceName());
        assertNotNull("CampusLoyola should not be null", obj);
        assertTrue("Object must be a Campus", obj instanceof Campus);
        Campus campus = (Campus) obj;
        assertEquals("Loyola campus", campus.getCampusName());
        List<BuildingName> expectedBuildings = Arrays.asList(
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
                BuildingName.PSYCHOLOGY_BUILDING);
        assertEquals(expectedBuildings, campus.getAssociatedBuildings());
        assertEquals(45.45863, campus.getLatitude(), 0.00001);
        assertEquals(-73.64188, campus.getLongitude(), 0.00001);
    }

    @Test
    public void testBuildingHall() {
        Object obj = resource.getObject(BuildingName.HALL.getResourceName());
        assertNotNull("BuildingHall should not be null", obj);
        assertTrue("Object must be a Building", obj instanceof Building);
        Building building = (Building) obj;
        // Verify building name and description content
        assertEquals("Hall building", building.getBuildingName());
        assertTrue(building.getDescription().contains("The Henry F. Hall Building"));
        // Verify campus association using getAssociatedCampus()
        assertEquals(CampusName.SGW, building.getAssociatedCampus());
        // Verify number of floors
        assertEquals(11, building.getFloors().size());
        // Verify coordinates
        assertEquals(45.49701, building.getLatitude(), 0.00001);
        assertEquals(-73.57877, building.getLongitude(), 0.00001);
        // Verify image resource and address
        assertEquals(2131231075, building.getBuildingImageRes());
        assertEquals("1455 De Maisonneuve Blvd W, Montreal, QC H3G 1M8", building.getBuildingAddress());
        // Verify the associated building enum
        assertEquals(BuildingName.HALL, building.getBuildingIdentifier());
    }

    @Test
    public void testBuildingJMSB() {
        Object obj = resource.getObject(BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName());
        assertNotNull("BuildingJMSB should not be null", obj);
        assertTrue("Object must be a Building", obj instanceof Building);
        Building building = (Building) obj;
        assertEquals("John Molson School of Business", building.getBuildingName());
        assertTrue(building.getDescription().contains("John Molson Building officially opened"));
        assertEquals(CampusName.SGW, building.getAssociatedCampus());
        // For JMSB there are 17 floor entries (S2, S1, and floors 1 to 15).
        assertEquals(17, building.getFloors().size());
        assertEquals(45.495323, building.getLatitude(), 0.00001);
        assertEquals(-73.579229, building.getLongitude(), 0.00001);
        assertEquals(2131231081, building.getBuildingImageRes());
        assertEquals("1450 Guy Street Montreal, QC H3H 0A1", building.getBuildingAddress());
        assertEquals(BuildingName.MOLSON_SCHOOL_OF_BUSINESS, building.getBuildingIdentifier());
    }

    @Test
    public void testBuildingCentral() {
        Object obj = resource.getObject(BuildingName.LOYOLA_CENTRAL_BUILDING.getResourceName());
        assertNotNull("BuildingCC should not be null", obj);
        assertTrue("Object must be a Building", obj instanceof Building);
        Building building = (Building) obj;
        assertEquals("Loyola Central building", building.getBuildingName());
        assertTrue(building.getDescription().contains("Central Building"));
        assertEquals(CampusName.LOYOLA, building.getAssociatedCampus());
        // Verify that there is one floor
        assertEquals(1, building.getFloors().size());
        assertEquals(45.45863, building.getLatitude(), 0.00001);
        assertEquals(-73.64066, building.getLongitude(), 0.00001);
        assertEquals(2131230947, building.getBuildingImageRes());
        assertEquals("7141 Sherbrooke St W, Montreal, Quebec H4B 2A7", building.getBuildingAddress());
        assertEquals(BuildingName.LOYOLA_CENTRAL_BUILDING, building.getBuildingIdentifier());
    }

    @Test
    public void testBuildingVanierLibrary() {
        Object obj = resource.getObject(BuildingName.VANIER_LIBRARY.getResourceName());
        assertNotNull("BuildingVL should not be null", obj);
        assertTrue("Object must be a Building", obj instanceof Building);
        Building building = (Building) obj;
        assertEquals("Vanier library building", building.getBuildingName());
        assertTrue(building.getDescription().contains("The library is named after Major-General"));
        assertEquals(CampusName.LOYOLA, building.getAssociatedCampus());
        // Verify that there are 3 floors
        assertEquals(3, building.getFloors().size());
        assertEquals(45.45891, building.getLatitude(), 0.00001);
        assertEquals(-73.63888, building.getLongitude(), 0.00001);
        assertEquals(2131230970, building.getBuildingImageRes());
        assertEquals("7141 Sherbrooke St W, Montreal, Quebec H4B 1R6", building.getBuildingAddress());
        assertEquals(BuildingName.VANIER_LIBRARY, building.getBuildingIdentifier());
    }

    @Test
    public void testBuildingVanierExtension() {
        Object obj = resource.getObject(BuildingName.VANIER_EXTENSION.getResourceName());
        assertNotNull("BuildingVE should not be null", obj);
        assertTrue("Object must be a Building", obj instanceof Building);
        Building building = (Building) obj;
        assertEquals("Vanier Extension Building", building.getBuildingName());
        assertTrue(building.getDescription().contains("Vanier Extension"));
        assertEquals(CampusName.LOYOLA, building.getAssociatedCampus());
        // Verify that there are 3 floors
        assertEquals(3, building.getFloors().size());
        assertEquals(45.45891, building.getLatitude(), 0.00001);
        assertEquals(-73.63888, building.getLongitude(), 0.00001);
        // Note: Vanier Extension uses the same drawable as Vanier Library.
        assertEquals(2131230969, building.getBuildingImageRes());
        assertEquals("7141 Rue Sherbrooke O, Montréal, QC H4B 1R6", building.getBuildingAddress());
        assertEquals(BuildingName.VANIER_EXTENSION, building.getBuildingIdentifier());
    }

    @Test
    public void testAllExpectedKeysPresent() {
        String[] expectedKeys = new String[] {
                CampusName.SGW.getResourceName(),
                CampusName.LOYOLA.getResourceName(),
                BuildingName.HALL.getResourceName(),
                BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName(),
                BuildingName.LOYOLA_CENTRAL_BUILDING.getResourceName(),
                BuildingName.VANIER_LIBRARY.getResourceName(),
                BuildingName.VANIER_EXTENSION.getResourceName()
        };
        for (String key : expectedKeys) {
            Object obj = resource.getObject(key);
            assertNotNull("Resource for key " + key + " should be non-null", obj);
        }
    }
}
