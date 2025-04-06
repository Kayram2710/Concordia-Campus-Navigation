package minicap.concordia.campusnav.map;

import android.content.Context;

import androidx.fragment.app.Fragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Collections;
import java.util.List;

import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.map.enums.MapColors;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class AbstractMapTest {

    // Dummy listener implementation to pass into the AbstractMap constructor.
    private static class DummyMapUpdateListener implements AbstractMap.MapUpdateListener {
        boolean onMapReadyCalled = false;
        String estimatedTime = null;
        String errorString = null;
        MapCoordinates clickedCoordinates = null;

        @Override
        public void onMapReady() {
            onMapReadyCalled = true;
        }

        @Override
        public void onEstimatedTimeUpdated(String newTime) {
            estimatedTime = newTime;
        }

        @Override
        public void onMapError(String errorString) {
            this.errorString = errorString;
        }

        @Override
        public void onMapClicked(MapCoordinates coordinates) {
            clickedCoordinates = coordinates;
        }
    }

    // Create a simple concrete subclass of AbstractMap for testing.
    private static class TestAbstractMap extends AbstractMap {

        public TestAbstractMap(MapUpdateListener listener) {
            super(listener);
        }

        @Override
        public Fragment initialize() {
            return new Fragment();
        }

        @Override
        public void addMarker(OutdoorPOI opoi) {
            // Dummy implementation.
        }

        @Override
        public void addMarker(MapCoordinates position, String title, MapColors color, boolean clearOtherMarkers) {
            // Dummy implementation.
        }

        @Override
        public void addMarker(MapCoordinates position, String title, MapColors color) {
            // Dummy implementation.
        }

        @Override
        public void addMarker(MapCoordinates position, String title, boolean clearOtherMarkers) {
            // Dummy implementation.
        }

        @Override
        public void addMarker(MapCoordinates position, String title) {
            // Dummy implementation.
        }

        @Override
        public void clearAllMarkers() {
            // Dummy implementation.
        }

        @Override
        public void clearPathFromMap() {
            // Dummy implementation.
        }

        @Override
        public void displayRoute(MapCoordinates origin, MapCoordinates destination, String travelMode) {
            // Dummy implementation.
        }

        @Override
        public void displayPOI(MapCoordinates origin, POIType type) {
            // Dummy implementation.
        }

        @Override
        public void centerOnCoordinates(MapCoordinates coordinates) {
            // Dummy implementation.
        }

        @Override
        public void switchToFloor(String floorName) {
            // Dummy implementation.
        }

        @Override
        public boolean toggleLocationTracking(boolean isEnabled) {
            return true;
        }
    }

    private TestAbstractMap testMap;
    private DummyMapUpdateListener dummyListener;
    private Context context;

    @Before
    public void setUp() {
        dummyListener = new DummyMapUpdateListener();
        testMap = new TestAbstractMap(dummyListener);
        context = RuntimeEnvironment.getApplication();
    }

    @Test
    public void testConstructorAndListener() {
        // Verify that the listener was set in the constructor.
        assertNotNull(testMap);
        assertEquals(dummyListener, testMap.listener);
    }

    @Test
    public void testInitialize() {
        // The dummy implementation returns a non-null Fragment.
        Fragment frag = testMap.initialize();
        assertNotNull(frag);
    }

    @Test
    public void testDefaultMethods() {
        // Call default (non-abstract) methods to ensure they execute without errors.
        // Since these methods are no-ops, we simply verify that no exceptions occur and return values are as expected.

        // setStyle: no return, just ensure it doesn't crash.
        testMap.setStyle(context, 123);

        // moveCameraToPosition: no return.
        testMap.moveCameraToPosition(10, new MapCoordinates(1.0, 2.0), 15.0f, 90.0f);

        // zoomToRouteSmoothly: no return.
        testMap.zoomToRouteSmoothly(Collections.emptyList());

        // createUserMarker: no return.
        testMap.createUserMarker(new MapCoordinates(1.0, 2.0), 456, context);

        // updateUserMarkerPosition: no return.
        testMap.updateUserMarkerPosition(new MapCoordinates(1.0, 2.0), context);

        // isUserMarkerNull: should return true as per implementation.
        assertTrue(testMap.isUserMarkerNull());

        // rotateUserMarker: no return.
        testMap.rotateUserMarker(45.0f);

        // getMapCoordinateFromMarker: should return null.
        assertNull(testMap.getMapCoordinateFromMarker());

        // clearAllPolylines: no return.
        testMap.clearAllPolylines();

        // addPolyline: no return.
        testMap.addPolyline(Collections.emptyList(), 5, 0, false);

        // decodePolyline: should return null.
        assertNull(testMap.decodePolyline("dummy"));

        // calculateRemainingDistance: should return 0.
        assertEquals(0f, testMap.calculateRemainingDistance(new MapCoordinates(1.0, 2.0)), 0.001f);

        // calculatePathBearing: should return 0.
        assertEquals(0f, testMap.calculatePathBearing(new MapCoordinates(1.0, 2.0)), 0.001f);
    }
}
