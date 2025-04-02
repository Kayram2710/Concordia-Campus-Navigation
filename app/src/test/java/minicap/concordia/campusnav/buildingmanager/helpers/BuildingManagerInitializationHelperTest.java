package minicap.concordia.campusnav.buildingmanager.helpers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;

public class BuildingManagerInitializationHelperTest {

    private AutoCloseable closeable; // For MockitoAnnotations if needed

    @Before
    public void setUp() {
        // Initialize any @Mock fields if you add them
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        // Closes mocks
        closeable.close();
    }

    /**
     * Test the happy path for createCampuses().
     * This assumes your real resource bundle is present and returns valid Campus objects.
     * If you actually have the resource, this test will pass and cover the normal path.
     */
    @Test
    public void testCreateCampuses_normal() {
        HashMap<CampusName, Campus> campuses = BuildingManagerInitializationHelper.createCampuses();
        assertNotNull("Returned campuses map should not be null", campuses);
        // We can’t guarantee the actual contents unless we have real resources,
        // but we can at least confirm no exception was thrown and coverage is gained.
    }

    /**
     * Test MissingResourceException in createCampuses().
     * We mock ResourceBundle.getBundle(...) to throw MissingResourceException,
     * ensuring the catch block is covered.
     */
    @Test
    public void testCreateCampuses_missingResource() {
        try (MockedStatic<ResourceBundle> mockedStatic = mockStatic(ResourceBundle.class)) {
            // Force getBundle(...) to throw MissingResourceException
            mockedStatic.when(() -> ResourceBundle.getBundle(any(String.class), any(Locale.class)))
                    .thenThrow(new MissingResourceException("Not found","Campus","CampusSGW"));

            // This should hit the catch block for MissingResourceException
            HashMap<CampusName, Campus> campuses = BuildingManagerInitializationHelper.createCampuses();
            assertNotNull("Even if missing resource, should return a non-null map", campuses);
            assertTrue("Should be empty due to exception", campuses.isEmpty());
        }
    }

    /**
     * Test ClassCastException in createCampuses().
     * We mock the ResourceBundle to return a String instead of a Campus.
     */
    @Test
    public void testCreateCampuses_classCast() {
        try (MockedStatic<ResourceBundle> mockedStatic = mockStatic(ResourceBundle.class)) {
            ResourceBundle mockBundle = mock(ResourceBundle.class);
            // Return a String instead of a Campus
            when(mockBundle.getObject("CampusSGW")).thenReturn("NotACampus");
            // For the other campus key to avoid a second exception, just return null or a dummy campus
            when(mockBundle.getObject("CampusLoyola")).thenReturn(null);

            // Force getBundle(...) to return our mock
            mockedStatic.when(() -> ResourceBundle.getBundle(any(String.class), any(Locale.class)))
                    .thenReturn(mockBundle);

            // Should hit the ClassCastException catch block for the first campus
            HashMap<CampusName, Campus> campuses = BuildingManagerInitializationHelper.createCampuses();
            assertNotNull("Should return a non-null map despite exception", campuses);
            assertTrue("Should be empty due to class cast error", campuses.isEmpty());
        }
    }

    /**
     * Test normal usage of createBuildings().
     */
    @Test
    public void testCreateBuildings_normal() {
        HashMap<BuildingName, Building> buildings = BuildingManagerInitializationHelper.createBuildings();
        assertNotNull("Returned buildings map should not be null", buildings);
        // We can’t guarantee the actual contents unless we have real resources.
    }

    /**
     * Test MissingResourceException in createBuildings().
     */
    @Test
    public void testCreateBuildings_missingResource() {
        try (MockedStatic<ResourceBundle> mockedStatic = mockStatic(ResourceBundle.class)) {
            mockedStatic.when(() -> ResourceBundle.getBundle(any(String.class), any(Locale.class)))
                    .thenThrow(new MissingResourceException("Not found","Building","BuildingHall"));

            HashMap<BuildingName, Building> buildings = BuildingManagerInitializationHelper.createBuildings();
            assertNotNull(buildings);
            assertTrue("Should be empty due to missing resource", buildings.isEmpty());
        }
    }

    /**
     * Test ClassCastException in createBuildings().
     */
    @Test
    public void testCreateBuildings_classCast() {
        try (MockedStatic<ResourceBundle> mockedStatic = mockStatic(ResourceBundle.class)) {
            ResourceBundle mockBundle = mock(ResourceBundle.class);
            // Return a String for the first building instead of a Building
            when(mockBundle.getObject("BuildingHall")).thenReturn("NotABuilding");
            // Return null or any other valid object for the other keys
            when(mockBundle.getObject("BuildingJMSB")).thenReturn(null);
            when(mockBundle.getObject("BuildingCC")).thenReturn(null);
            when(mockBundle.getObject("BuildingVL")).thenReturn(null);
            when(mockBundle.getObject("BuildingVE")).thenReturn(null);

            mockedStatic.when(() -> ResourceBundle.getBundle(any(String.class), any(Locale.class)))
                    .thenReturn(mockBundle);

            HashMap<BuildingName, Building> buildings = BuildingManagerInitializationHelper.createBuildings();
            assertNotNull(buildings);
            assertTrue("Should be empty due to class cast error", buildings.isEmpty());
        }
    }

    /**
     * Test createOutdoorPOIs() is empty but not null.
     */
    @Test
    public void testCreateOutdoorPOIs() {
        ArrayList<OutdoorPOI> outdoorPOIs = BuildingManagerInitializationHelper.createOutdoorPOIs();
        assertNotNull("Should return an ArrayList, not null", outdoorPOIs);
        assertTrue("Initially, it should be empty", outdoorPOIs.isEmpty());
    }
}
