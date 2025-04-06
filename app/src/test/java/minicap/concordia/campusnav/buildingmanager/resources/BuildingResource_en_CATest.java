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
        // There should be 7 keys defined: 2 campuses and 5 buildings.
        assertEquals(7, contents.length);
    }

    @Test
    public void testCampusSGW() {
        Object obj = resource.getObject(CampusName.SGW.getResourceName());
        assertNotNull("CampusSGW should not be null", obj);
        assertTrue("Object must be a Campus", obj instanceof Campus);
        Campus campus = (Campus) obj;
        assertEquals("Sir George William campus", campus.getCampusName());
        List<BuildingName> expectedBuildings = Arrays.asList(
                BuildingName.HALL,
                BuildingName.MOLSON_SCHOOL_OF_BUSINESS);
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
                BuildingName.VANIER_EXTENSION,
                BuildingName.VANIER_LIBRARY);
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
        assertEquals(R.drawable.hallbuilding, building.getBuildingImageRes());
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
        assertEquals(R.drawable.jmsb, building.getBuildingImageRes());
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
        assertEquals(R.drawable.loy_central, building.getBuildingImageRes());
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
        assertEquals(R.drawable.vanier_library, building.getBuildingImageRes());
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
        assertEquals(R.drawable.vanier_library, building.getBuildingImageRes());
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
