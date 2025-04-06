package minicap.concordia.campusnav.buildingshape;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CampusBuildingShapesTest {

    @Before
    public void setUp() throws Exception {
        // The CampusBuildingShapes class’s static blocks load resource bundles.
        // To avoid MissingResourceException (if the real bundles aren’t available in test),
        // override the static lists with dummy data.
        List<PolygonOptions> dummySgwList = new ArrayList<>();
        dummySgwList.add(new PolygonOptions());
        List<PolygonOptions> dummyLoyolaList = new ArrayList<>();
        dummyLoyolaList.add(new PolygonOptions());

        Field sgwField = CampusBuildingShapes.class.getDeclaredField("sgwBuildingCoordinates");
        sgwField.setAccessible(true);
        sgwField.set(null, dummySgwList);

        Field loyolaField = CampusBuildingShapes.class.getDeclaredField("loyolaBuildingCoordinates");
        loyolaField.setAccessible(true);
        loyolaField.set(null, dummyLoyolaList);
    }

    @Test
    public void testGetSgwBuildingCoordinates() {
        List<PolygonOptions> sgwCoords = CampusBuildingShapes.getSgwBuildingCoordinates();
        assertNotNull("SGW coordinates should not be null", sgwCoords);
        // Since we overrode the static list in setUp, it should contain exactly one element.
        assertEquals("SGW coordinates size", 1, sgwCoords.size());
    }

    @Test
    public void testGetLoyolaBuildingCoordinates() {
        List<PolygonOptions> loyolaCoords = CampusBuildingShapes.getLoyolaBuildingCoordinates();
        assertNotNull("Loyola coordinates should not be null", loyolaCoords);
        assertEquals("Loyola coordinates size", 1, loyolaCoords.size());
    }

    @Test
    public void testGetBuildingNameAtLocation_found() {
        // Create a dummy ResourceBundle for SGW that returns one entry.
        ResourceBundle dummySgwBundle = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                if ("TestBuilding".equals(key)) {
                    // Create a dummy polygon (triangle) that should contain our test location.
                    PolygonOptions poly = new PolygonOptions();
                    poly.add(new LatLng(0, 0));
                    poly.add(new LatLng(0, 1));
                    poly.add(new LatLng(1, 0));
                    return poly;
                }
                return null;
            }

            @Override
            public Enumeration<String> getKeys() {
                return Collections.enumeration(Arrays.asList("TestBuilding"));
            }
        };

        // Create a dummy (empty) ResourceBundle for Loyola.
        ResourceBundle dummyLoyolaBundle = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                return null;
            }

            @Override
            public Enumeration<String> getKeys() {
                return Collections.emptyEnumeration();
            }
        };

        try (MockedStatic<ResourceBundle> rbMock = mockStatic(ResourceBundle.class);
             MockedStatic<PolyUtil> polyUtilMock = mockStatic(PolyUtil.class)) {

            // When the code calls ResourceBundle.getBundle, return our dummy bundles.
            rbMock.when(() -> ResourceBundle.getBundle("minicap.concordia.campusnav.buildingshape.SGWCoordinatesResource_en_CA"))
                    .thenReturn(dummySgwBundle);
            rbMock.when(() -> ResourceBundle.getBundle("minicap.concordia.campusnav.buildingshape.LoyolaCoordinatesResource_en_CA"))
                    .thenReturn(dummyLoyolaBundle);

            // Simulate that PolyUtil.containsLocation returns true (i.e. the test location is inside the polygon).
            polyUtilMock.when(() -> PolyUtil.containsLocation(any(LatLng.class), anyList(), eq(true)))
                    .thenReturn(true);

            // Use a location that should be contained in the dummy polygon.
            LatLng testLocation = new LatLng(0.1, 0.1);
            String buildingName = CampusBuildingShapes.getBuildingNameAtLocation(testLocation);
            assertEquals("TestBuilding", buildingName);
        }
    }

    @Test
    public void testGetBuildingNameAtLocation_notFound() {
        // Create dummy ResourceBundles that simulate polygons which do NOT contain the test location.
        ResourceBundle dummySgwBundle = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                // Return a dummy polygon (triangle) for testing.
                PolygonOptions poly = new PolygonOptions();
                poly.add(new LatLng(0, 0));
                poly.add(new LatLng(0, 1));
                poly.add(new LatLng(1, 0));
                return poly;
            }

            @Override
            public Enumeration<String> getKeys() {
                return Collections.enumeration(Arrays.asList("TestBuilding"));
            }
        };

        ResourceBundle dummyLoyolaBundle = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                return null;
            }

            @Override
            public Enumeration<String> getKeys() {
                return Collections.emptyEnumeration();
            }
        };

        try (MockedStatic<ResourceBundle> rbMock = mockStatic(ResourceBundle.class);
             MockedStatic<PolyUtil> polyUtilMock = mockStatic(PolyUtil.class)) {

            rbMock.when(() -> ResourceBundle.getBundle("minicap.concordia.campusnav.buildingshape.SGWCoordinatesResource_en_CA"))
                    .thenReturn(dummySgwBundle);
            rbMock.when(() -> ResourceBundle.getBundle("minicap.concordia.campusnav.buildingshape.LoyolaCoordinatesResource_en_CA"))
                    .thenReturn(dummyLoyolaBundle);

            // Simulate that PolyUtil.containsLocation always returns false (location not inside any polygon).
            polyUtilMock.when(() -> PolyUtil.containsLocation(any(LatLng.class), anyList(), eq(true)))
                    .thenReturn(false);

            LatLng testLocation = new LatLng(10, 10);
            String buildingName = CampusBuildingShapes.getBuildingNameAtLocation(testLocation);
            assertNull("No building should be found", buildingName);
        }
    }
}
