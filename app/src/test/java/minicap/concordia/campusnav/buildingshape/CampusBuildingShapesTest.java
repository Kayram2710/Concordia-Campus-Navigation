package minicap.concordia.campusnav.buildingshape;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;

/**
 * Single test class for CampusBuildingShapes.
 *
 * This test uses Mockito's static mocking to intercept calls to
 * ResourceBundle.getBundle(...) and supply dummy resource bundles that
 * contain all expected keys.
 */
public class CampusBuildingShapesTest {

    private static MockedStatic<ResourceBundle> mockedResourceBundle;

    // Expected keys for the SGW bundle.
    private static final String[] SGW_KEYS = {
            "bAnnex", "ciAnnex", "clAnnex", "dAnnex", "enAnnex", "erBuilding",
            "evBuilding", "faAnnex", "fbBuilding", "fgBuilding", "greyNunsAnnex",
            "gmBuilding", "gnBuilding", "gsBuilding", "Hall Building", "kAnnex",
            "lbBuilding", "ldBuilding", "lsBuilding", "mAnnex", "John Molson Building",
            "miAnnex", "muAnnex", "pAnnex", "prAnnex", "qAnnex", "rAnnex", "rrAnnex",
            "sAnnex", "sbBuilding", "tAnnex", "tdBuilding", "vAnnex", "vaBuilding",
            "xAnnex", "zAnnex"
    };

    // Expected keys for the Loyola bundle.
    private static final String[] LOYOLA_KEYS = {
            "adBuilding", "bbAnnex", "bhAnnex", "Loyola Central Building", "cjBuilding",
            "doDome", "fcBuilding", "geBuilding", "haBuilding", "hbBuilding",
            "hcBuilding", "huBuilding", "jrResidence", "pcBuilding", "psBuilding",
            "Vanier Library/Extension Building", "pyBuilding", "raBuilding", "rfBuilding",
            "siBuilding", "spBuilding", "taBuilding"
    };

    /**
     * Helper method to create a square polygon given a start latitude/longitude and side length.
     */
    private static PolygonOptions createPolygon(double startLat, double startLng, double delta) {
        return new PolygonOptions()
                .add(new LatLng(startLat, startLng))
                .add(new LatLng(startLat, startLng + delta))
                .add(new LatLng(startLat + delta, startLng + delta))
                .add(new LatLng(startLat + delta, startLng));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Start static mocking for ResourceBundle.
        mockedResourceBundle = mockStatic(ResourceBundle.class);

        // Create a dummy SGW ResourceBundle.
        ResourceBundle testSGWBundle = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                // For key "bAnnex", return a polygon that covers (0,0)-(1,1) to match our test.
                if ("bAnnex".equals(key)) {
                    return createPolygon(0, 0, 1);
                }
                // For all other keys, return a polygon far away.
                return createPolygon(100, 100, 1);
            }

            @Override
            public Enumeration<String> getKeys() {
                return Collections.enumeration(Arrays.asList(SGW_KEYS));
            }
        };

        // Create a dummy Loyola ResourceBundle.
        ResourceBundle testLoyolaBundle = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                // For key "adBuilding", return a polygon covering (10,10)-(11,11).
                if ("adBuilding".equals(key)) {
                    return createPolygon(10, 10, 1);
                }
                // For all other keys, return a polygon far away.
                return createPolygon(200, 200, 1);
            }

            @Override
            public Enumeration<String> getKeys() {
                return Collections.enumeration(Arrays.asList(LOYOLA_KEYS));
            }
        };

        // Set up the mocked behavior for ResourceBundle.getBundle(...) calls.
        mockedResourceBundle.when(() ->
                        ResourceBundle.getBundle("minicap.concordia.campusnav.buildingshape.SGWCoordinatesResource_en_CA"))
                .thenReturn(testSGWBundle);

        mockedResourceBundle.when(() ->
                        ResourceBundle.getBundle("minicap.concordia.campusnav.buildingshape.LoyolaCoordinatesResource_en_CA"))
                .thenReturn(testLoyolaBundle);

        // Clear cache to force our mocks to be used.
        ResourceBundle.clearCache();

        // Force initialization of CampusBuildingShapes (its static blocks use the ResourceBundle mocks).
        Class.forName("minicap.concordia.campusnav.buildingshape.CampusBuildingShapes");
    }

    @AfterClass
    public static void tearDownClass() {
        if (mockedResourceBundle != null) {
            mockedResourceBundle.close();
        }
    }

    @Test
    public void testGetSgwBuildingCoordinates() {
        List<PolygonOptions> sgwCoords = CampusBuildingShapes.getSgwBuildingCoordinates();
        assertNotNull("SGW building coordinates list should not be null", sgwCoords);
        // Expecting one polygon for each key in our SGW dummy bundle.
        assertEquals("Expected 37 SGW building polygons", SGW_KEYS.length, sgwCoords.size());
    }

    @Test
    public void testGetLoyolaBuildingCoordinates() {
        List<PolygonOptions> loyolaCoords = CampusBuildingShapes.getLoyolaBuildingCoordinates();
        assertNotNull("Loyola building coordinates list should not be null", loyolaCoords);
        // Expecting one polygon for each key in our Loyola dummy bundle.
        assertEquals("Expected 22 Loyola building polygons", LOYOLA_KEYS.length, loyolaCoords.size());
    }

    @Test
    public void testGetBuildingNameAtLocation_SGW() {
        // A point inside the "bAnnex" polygon (covers (0,0)-(1,1)).
        LatLng insideSgw = new LatLng(0.5, 0.5);
        String buildingName = CampusBuildingShapes.getBuildingNameAtLocation(insideSgw);
        // The SGW bundle is checked first; we expect "bAnnex" to be returned.
        assertEquals("Expected SGW building name", "bAnnex", buildingName);
    }

    @Test
    public void testGetBuildingNameAtLocation_Loyola() {
        // A point not in any SGW polygon (since "bAnnex" covers (0,0)-(1,1) and others are far away)
        // but inside the "adBuilding" polygon in the Loyola bundle (covers (10,10)-(11,11)).
        LatLng insideLoyola = new LatLng(10.5, 10.5);
        String buildingName = CampusBuildingShapes.getBuildingNameAtLocation(insideLoyola);
        assertEquals("Expected Loyola building name", "adBuilding", buildingName);
    }

    @Test
    public void testGetBuildingNameAtLocation_NotFound() {
        // A point that doesn't fall within any dummy polygon.
        LatLng outside = new LatLng(50, 50);
        String buildingName = CampusBuildingShapes.getBuildingNameAtLocation(outside);
        assertNull("Expected null for location not in any building", buildingName);
    }
}
