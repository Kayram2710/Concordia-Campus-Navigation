package minicap.concordia.campusnav.map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.MockedStatic;


import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.screens.NavigationActivity;
import minicap.concordia.campusnav.map.enums.MapColors;

@RunWith(RobolectricTestRunner.class)
public class NavigationGoogleMapsTest {

    private NavigationGoogleMaps navMaps;
    private GoogleMap mockGoogleMap;
    private UiSettings mockUiSettings;
    private Context context;
    private DummyMapUpdateListener dummyListener;
    private MockedStatic<BitmapDescriptorFactory> mockedBitmapDescriptorFactory;
    private MockedStatic<CameraUpdateFactory> mockedCameraUpdateFactory;

    // A dummy listener to record callback events.
    private static class DummyMapUpdateListener implements AbstractMap.MapUpdateListener {
        boolean mapReadyCalled = false;
        String estimatedTime = null;
        MapCoordinates mapClicked = null;

        @Override
        public void onMapElementLoaded() {

        }

        @Override
        public void onMapReady() { mapReadyCalled = true; }
        @Override
        public void onEstimatedTimeUpdated(String newTime) { estimatedTime = newTime; }
        @Override
        public void onMapError(String errorString) { }
        @Override
        public void onMapClicked(MapCoordinates coordinates) { mapClicked = coordinates; }
    }

    // A simple TestMapCoordinates for testing.
    private static class TestMapCoordinates extends MapCoordinates {
        private final double lat;
        private final double lng;
        public TestMapCoordinates(double lat, double lng) {
            super(lat, lng);
            this.lat = lat;
            this.lng = lng;
        }
        @Override
        public LatLng toGoogleMapsLatLng() {
            return new LatLng(lat, lng);
        }
    }

    @Before
    public void setUp() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        context = RuntimeEnvironment.getApplication();
        dummyListener = new DummyMapUpdateListener();
        navMaps = new NavigationGoogleMaps(dummyListener);

        // Create a mock GoogleMap and its UiSettings.
        mockGoogleMap = mock(GoogleMap.class);
        mockUiSettings = mock(UiSettings.class);
        when(mockGoogleMap.getUiSettings()).thenReturn(mockUiSettings);

        // Use the superclass method to set the map.
        navMaps.setMap(mockGoogleMap);

        mockedBitmapDescriptorFactory = mockStatic(BitmapDescriptorFactory.class);
        mockedBitmapDescriptorFactory.when(BitmapDescriptorFactory::defaultMarker).thenReturn(mock(com.google.android.gms.maps.model.BitmapDescriptor.class));

        mockedCameraUpdateFactory = mockStatic(CameraUpdateFactory.class);
        mockedCameraUpdateFactory.when(() -> CameraUpdateFactory.newLatLngZoom(any(LatLng.class), anyFloat())).thenReturn(mock(CameraUpdate.class));
        mockedCameraUpdateFactory.when(() -> CameraUpdateFactory.newCameraPosition(any(CameraPosition.class))).thenReturn(mock(CameraUpdate.class));
    }

    @After
    public void tearDown() {
        mockedBitmapDescriptorFactory.close();
        mockedCameraUpdateFactory.close();
    }

    @Test
    public void testOnMapReady() {
        navMaps.onMapReady(mockGoogleMap);
        verify(mockUiSettings).setZoomControlsEnabled(true);
        verify(mockUiSettings).setCompassEnabled(true);
        verify(mockGoogleMap).setMapType(GoogleMap.MAP_TYPE_NORMAL);
        assertTrue(dummyListener.mapReadyCalled);
    }

    @Test
    public void testMoveCameraToPosition() {
        TestMapCoordinates coords = new TestMapCoordinates(45.0, -73.0);
        navMaps.moveCameraToPosition(50, coords, 15.0f, 90.0f);
        // animateCamera and setPadding calls
        verify(mockGoogleMap).animateCamera(any(CameraUpdate.class), eq(200), isNull());
        verify(mockGoogleMap).setPadding(0, 50, 0, 0);
    }

    @Test
    public void testCreateUserMarker() {
        Marker fakeMarker = mock(Marker.class);
        when(mockGoogleMap.addMarker(any(MarkerOptions.class))).thenReturn(fakeMarker);

        TestMapCoordinates coords = new TestMapCoordinates(45.0, -73.0);
        navMaps.createUserMarker(coords, R.drawable.token, context);

        assertFalse(navMaps.isUserMarkerNull());
    }

    @Test
    public void testUpdateUserMarkerPosition() throws Exception {
        // Reflectively set userMarker to null => triggers createUserMarker
        Field markerField = NavigationGoogleMaps.class.getDeclaredField("userMarker");
        markerField.setAccessible(true);
        markerField.set(navMaps, null);

        Marker fakeMarker = mock(Marker.class);
        when(mockGoogleMap.addMarker(any(MarkerOptions.class))).thenReturn(fakeMarker);

        TestMapCoordinates coords = new TestMapCoordinates(45.0, -73.0);
        navMaps.updateUserMarkerPosition(coords, context);
        assertFalse(navMaps.isUserMarkerNull());

        // Now userMarker is not null => update position
        markerField.set(navMaps, fakeMarker);
        navMaps.updateUserMarkerPosition(coords, context);
        verify(fakeMarker).setPosition(coords.toGoogleMapsLatLng());
    }

    @Test
    public void testRotateUserMarker() throws Exception {
        Marker fakeMarker = mock(Marker.class);
        Field markerField = NavigationGoogleMaps.class.getDeclaredField("userMarker");
        markerField.setAccessible(true);
        markerField.set(navMaps, fakeMarker);

        navMaps.rotateUserMarker(45.0f);
        verify(fakeMarker).setRotation(45.0f);
    }

    @Test
    public void testGetMapCoordinateFromMarker() throws Exception {
        Marker fakeMarker = mock(Marker.class);
        when(fakeMarker.getPosition()).thenReturn(new LatLng(45.0, -73.0));

        Field markerField = NavigationGoogleMaps.class.getDeclaredField("userMarker");
        markerField.setAccessible(true);
        markerField.set(navMaps, fakeMarker);

        MapCoordinates mc = navMaps.getMapCoordinateFromMarker();
        assertNotNull(mc);
        assertEquals(45.0, mc.getLat(), 0.001);
        assertEquals(-73.0, mc.getLng(), 0.001);
    }

    @Test
    public void testClearAllPolylines() throws Exception {
        // The superclass (InternalGoogleMaps) has a "polylines" field.
        Polyline fakePolyline = mock(Polyline.class);
        List<Polyline> polylineList = new ArrayList<>();
        polylineList.add(fakePolyline);

        Field polylinesField = InternalGoogleMaps.class.getDeclaredField("polylines");
        polylinesField.setAccessible(true);
        polylinesField.set(navMaps, polylineList);

        navMaps.clearAllPolylines();
        verify(fakePolyline).remove();

        List<Polyline> afterList = (List<Polyline>) polylinesField.get(navMaps);
        assertTrue(afterList.isEmpty());
    }

    @Test
    public void testAddPolyline() {
        Marker fakeMarker = mock(Marker.class);
        List<MapCoordinates> coordsList = new ArrayList<>();
        coordsList.add(new TestMapCoordinates(45.0, -73.0));
        coordsList.add(new TestMapCoordinates(46.0, -74.0));

        Polyline fakePolyline = mock(Polyline.class);
        when(mockGoogleMap.addPolyline(any(PolylineOptions.class))).thenReturn(fakePolyline);

        navMaps.addPolyline(coordsList, 10, Color.RED, true);
        verify(mockGoogleMap).addPolyline(argThat(options ->
                options.getPoints().size() == 2 &&
                        options.getWidth() == 10 &&
                        options.getColor() == Color.RED &&
                        options.isGeodesic()
        ));
    }

    @Test
    public void testDecodePolyline() {
        String encoded = "a~l~Fjk~uOwHJy@P"; // known sample
        List<MapCoordinates> decoded = navMaps.decodePolyline(encoded);
        assertNotNull(decoded);
        assertFalse(decoded.isEmpty());
    }

    @Test
    public void testCalculateRemainingDistance() {
        //TODO Might not be possible
    }

    @Test
    public void testCalculatePathBearing() throws Exception {
        Polyline fakePolyline = mock(Polyline.class);
        List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(45.0, -73.0));
        points.add(new LatLng(45.002, -73.002));
        when(fakePolyline.getPoints()).thenReturn(points);

        Field polylinesField = InternalGoogleMaps.class.getDeclaredField("polylines");
        polylinesField.setAccessible(true);
        polylinesField.set(navMaps, new ArrayList<>(List.of(fakePolyline)));

        MapCoordinates currentPos = new TestMapCoordinates(45.0, -73.0);
        float bearing = navMaps.calculatePathBearing(currentPos);

        double expected = SphericalUtil.computeHeading(
                new LatLng(45.0, -73.0), new LatLng(45.002, -73.002));
        assertEquals((float) expected, bearing, 1.0f);
    }

    @Test
    public void testBitmapDescriptorFromVector() throws Exception {
        // Test private method via reflection
        Method method = NavigationGoogleMaps.class.getDeclaredMethod("bitmapDescriptorFromVector", Context.class, int.class);
        method.setAccessible(true);

        // Provide an invalid resource ID => should return null
        Object result = method.invoke(navMaps, context, -1);
        assertNull(result);
    }
}
