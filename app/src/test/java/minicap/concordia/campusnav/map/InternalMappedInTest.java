package minicap.concordia.campusnav.map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.mappedin.sdk.MPIMapView;
import com.mappedin.sdk.models.MPIMap;
import com.mappedin.sdk.models.MPIMapClickEvent;
import com.mappedin.sdk.models.MPIMap.MPICoordinate;
import com.mappedin.sdk.web.MPIOptions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import minicap.concordia.campusnav.map.enums.MapColors;
import minicap.concordia.campusnav.map.helpers.MapColorConversionHelper;

/**
 * Example unit tests for InternalMappedIn, mocking the MappedIn SDK calls.
 */

public class InternalMappedInTest {

    // Mock interfaces for the marker and blue dot managers (from MPIMapView).
    public interface MarkerManager {
        void addByCoordinate(MPICoordinate coord, String html, MPIOptions.Marker markerOptions, Object unused);
        void removeAll();
    }

    public interface BlueDotManager {
        void enable(MPIOptions.BlueDot options);
        void disable();
    }

    // Minimal MapUpdateListener that captures onMapClicked and onMapReady calls
    private static class TestMapUpdateListener implements AbstractMap.MapUpdateListener {
        boolean mapReadyCalled = false;
        MapCoordinates clickedCoordinates = null;

        @Override
        public void onMapReady() {
            mapReadyCalled = true;
        }

        @Override
        public void onEstimatedTimeUpdated(String newTime) {
            // not used here
        }

        @Override
        public void onMapError(String errorString) {
            // not used here
        }

        @Override
        public void onMapClicked(MapCoordinates coordinates) {
            clickedCoordinates = coordinates;
        }
    }

    // Subclass exposing a setter for the private 'curMap' field in InternalMappedIn
    private static class TestInternalMappedIn extends InternalMappedIn {
        public TestInternalMappedIn(MapUpdateListener listener) {
            super(listener);
        }

        public void setMapView(MPIMapView mapView) {
            try {
                // Reflectively set the 'curMap' field
                java.lang.reflect.Field field = InternalMappedIn.class.getDeclaredField("curMap");
                field.setAccessible(true);
                field.set(this, mapView);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Mock
    private MPIMapView mockMapView;

    @Mock
    private MPIMap mockMPIMap;

    // Our custom MarkerManager & BlueDotManager, which we will link to MPIMapView via stubbing
    @Mock
    private MarkerManager mockMarkerManager;

    @Mock
    private BlueDotManager mockBlueDotManager;

    private TestMapUpdateListener testListener;
    private TestInternalMappedIn testInternalMappedIn;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testListener = new TestMapUpdateListener();
        testInternalMappedIn = new TestInternalMappedIn(testListener);

        // By default, we can have a mocked MPIMap returned by getCurrentMap()
        when(mockMapView.getCurrentMap()).thenReturn(mockMPIMap);

        // Simulate that MPIMapView has a markerManager & blueDotManager:
        // You might store them in your real code differently,
        // but for test we just pretend these calls exist:
        when(mockMapView.getMarkerManager()).thenAnswer(invocation -> mockMarkerManager);
        when(mockMapView.getBlueDotManager()).thenAnswer(invocation -> mockBlueDotManager);

        // Put the mocked MPIMapView into our test instance
        testInternalMappedIn.setMapView(mockMapView);
    }

    @Test
    public void testOnFirstMapLoaded_callsListenerOnMapReady() {
        // internalMappedIn calls onFirstMapLoaded() once the map is loaded
        testInternalMappedIn.onFirstMapLoaded();

        assertTrue("Listener's onMapReady should be called", testListener.mapReadyCalled);
    }

    @Test
    public void testOnClick_convertsEventToMapCoordinates() {
        // Create a mock event with a dummy MPICoordinate
        double lat = 10.0, lng = 20.0, x = 1.0, y = 2.0;
        MPICoordinate mockCoord = new MPICoordinate(x, y, lat, lng, mockMPIMap);

        MPIMapClickEvent mockClickEvent = mock(MPIMapClickEvent.class);
        when(mockClickEvent.getPosition()).thenReturn(mockCoord);

        // Trigger the click
        testInternalMappedIn.onClick(mockClickEvent);

        // Verify the listener got the correct lat/lng
        assertNotNull(testListener.clickedCoordinates);
        assertEquals(lat, testListener.clickedCoordinates.getLat(), 0.0001);
        assertEquals(lng, testListener.clickedCoordinates.getLng(), 0.0001);
    }

    @Test
    public void testAddMarker_callsAddByCoordinate() {
        // Provide sample coordinates
        MapCoordinates coords = new MapCoordinates(45.5, -73.5);
        String title = "Test Marker";
        MapColors color = MapColors.BLUE;
        String expectedHtml = MapColorConversionHelper.getMappedInMarkerHTML(color, title);

        // Call addMarker
        testInternalMappedIn.addMarker(coords, title, color, false);

        // Capture the arguments used in addByCoordinate
        ArgumentCaptor<MPICoordinate> coordCaptor = ArgumentCaptor.forClass(MPICoordinate.class);
        ArgumentCaptor<String> htmlCaptor = ArgumentCaptor.forClass(String.class);

        verify(mockMarkerManager, times(1)).addByCoordinate(
                coordCaptor.capture(),
                htmlCaptor.capture(),
                any(MPIOptions.Marker.class),
                isNull()
        );

        // Check the HTML
        assertEquals(expectedHtml, htmlCaptor.getValue());

        // Check lat/lng in the captured coordinate
        MPICoordinate capturedCoord = coordCaptor.getValue();
        assertEquals(coords.getLat(), capturedCoord.getLatitude(), 0.0001);
        assertEquals(coords.getLng(), capturedCoord.getLongitude(), 0.0001);
    }

    @Test
    public void testClearAllMarkers() {
        testInternalMappedIn.clearAllMarkers();
        verify(mockMarkerManager, times(1)).removeAll();
    }

    @Test
    public void testToggleLocationTracking_enable() {
        boolean result = testInternalMappedIn.toggleLocationTracking(true);

        assertTrue(result);
        verify(mockBlueDotManager, times(1)).enable(any(MPIOptions.BlueDot.class));
        verify(mockBlueDotManager, never()).disable();
    }

    @Test
    public void testToggleLocationTracking_disable() {
        boolean result = testInternalMappedIn.toggleLocationTracking(false);

        assertTrue(result);
        verify(mockBlueDotManager, times(1)).disable();
        verify(mockBlueDotManager, never()).enable(any(MPIOptions.BlueDot.class));
    }
}
