package minicap.concordia.campusnav.buildingmanager.resources;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingResourceException;

import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingmanager.helpers.BuildingManagerInitializationHelper;

public class BuildingResource_en_CATest {

    @Test
    public void testCreateCampuses_MissingResource() {
        // the method should catch exceptions and return an empty map.
        HashMap<CampusName, ?> campuses = BuildingManagerInitializationHelper.createCampuses();
        assertNotNull("Returned campus map should not be null", campuses);
        assertTrue("Returned campus map should be empty when resources are missing", campuses.isEmpty());
    }

    @Test
    public void testCreateBuildings_MissingResource() {
        // if building resources are missing, an empty map is returned.
        HashMap<BuildingName, ?> buildings = BuildingManagerInitializationHelper.createBuildings();
        assertNotNull("Returned building map should not be null", buildings);
        assertTrue("Returned building map should be empty when resources are missing", buildings.isEmpty());
    }

    @Test
    public void testCreateOutdoorPOIs() {
        // This method always returns an empty ArrayList.
        ArrayList<?> outdoorPOIs = BuildingManagerInitializationHelper.createOutdoorPOIs();
        assertNotNull("Returned outdoor POI list should not be null", outdoorPOIs);
        assertTrue("Returned outdoor POI list should be empty", outdoorPOIs.isEmpty());
    }
}
