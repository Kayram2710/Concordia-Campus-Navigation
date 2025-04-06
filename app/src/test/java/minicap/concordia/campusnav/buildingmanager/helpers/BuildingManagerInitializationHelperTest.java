package minicap.concordia.campusnav.buildingmanager.helpers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;

public class BuildingManagerInitializationHelperTest {

    @Test
    public void testCreateCampuses_MissingResource() {
        // When the resource bundle for campuses is missing,
        // the method should catch MissingResourceException and return an empty map.
        HashMap<CampusName, ?> campuses = BuildingManagerInitializationHelper.createCampuses();
        assertNotNull("Returned map should not be null", campuses);
        assertTrue("Returned map should be empty when resources are missing", campuses.isEmpty());
    }

    @Test
    public void testCreateBuildings_MissingResource() {
        // When the resource bundle for buildings or indoor POIs is missing,
        // the method should catch MissingResourceException (or ClassCastException) and return an empty map.
        HashMap<BuildingName, ?> buildings = BuildingManagerInitializationHelper.createBuildings();
        assertNotNull("Returned map should not be null", buildings);
        assertTrue("Returned map should be empty when resources are missing", buildings.isEmpty());
    }

    @Test
    public void testCreateOutdoorPOIs() {
        // This method always returns an empty ArrayList.
        ArrayList<?> outdoorPOIs = BuildingManagerInitializationHelper.createOutdoorPOIs();
        assertNotNull("Returned list should not be null", outdoorPOIs);
        assertTrue("Returned list should be empty", outdoorPOIs.isEmpty());
    }
}
