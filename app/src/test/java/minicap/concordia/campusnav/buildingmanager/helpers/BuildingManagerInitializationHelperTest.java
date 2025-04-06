package minicap.concordia.campusnav.buildingmanager.helpers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.junit.Test;
import org.mockito.MockedStatic;

import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;

public class BuildingManagerInitializationHelperTest {

    @Test
    public void testCreateCampuses_MissingResource() {
        // Force ResourceBundle.getBundle to throw MissingResourceException
        try (MockedStatic<ResourceBundle> mockedBundle = mockStatic(ResourceBundle.class)) {
            mockedBundle.when(() -> ResourceBundle.getBundle(anyString(), (Locale) any()))
                    .thenThrow(new MissingResourceException("Not found", "Dummy", "key"));

            HashMap<CampusName, ?> campuses = BuildingManagerInitializationHelper.createCampuses();
            assertNotNull("Returned map should not be null", campuses);
            assertTrue("Returned map should be empty when resources are missing", campuses.isEmpty());
        }
    }

    @Test
    public void testCreateBuildings_MissingResource() {
        // Force ResourceBundle.getBundle to throw MissingResourceException
        try (MockedStatic<ResourceBundle> mockedBundle = mockStatic(ResourceBundle.class)) {
            mockedBundle.when(() -> ResourceBundle.getBundle(anyString(), (Locale) any()))
                    .thenThrow(new MissingResourceException("Not found", "Dummy", "key"));

            HashMap<BuildingName, ?> buildings = BuildingManagerInitializationHelper.createBuildings();
            assertNotNull("Returned map should not be null", buildings);
            assertTrue("Returned map should be empty when resources are missing", buildings.isEmpty());
        }
    }

    @Test
    public void testCreateOutdoorPOIs() {
        // This method always returns an empty ArrayList.
        ArrayList<?> outdoorPOIs = BuildingManagerInitializationHelper.createOutdoorPOIs();
        assertNotNull("Returned list should not be null", outdoorPOIs);
        assertTrue("Returned list should be empty", outdoorPOIs.isEmpty());
    }
}
