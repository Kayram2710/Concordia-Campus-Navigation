package minicap.concordia.campusnav.map;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InternalGoogleMapsTests {

    @Mock
    private AbstractMap.MapUpdateListener mockListener;

    @Mock
    private GoogleMap mapMock;

    @Mock
    private UiSettings uiSettingsMock;

    @Before
    public void setup() {
        Mockito.reset(mockListener);
        Mockito.reset(mapMock);
        Mockito.when(mapMock.getUiSettings()).thenReturn(uiSettingsMock);
    }

    @Test
    public void testCenterOnCoordinates() {
        GoogleMap mapMock = Mockito.mock(GoogleMap.class);
        MapCoordinates updatedCoors = new MapCoordinates(1, 1);
        CameraUpdate mockUpdate = Mockito.mock(CameraUpdate.class);
        AbstractMap.MapUpdateListener mockListener = Mockito.mock(AbstractMap.MapUpdateListener.class);

        try (MockedStatic<CameraUpdateFactory> staticMock = Mockito.mockStatic(CameraUpdateFactory.class)) {
            staticMock.when(() -> CameraUpdateFactory.newCameraPosition(Mockito.any(CameraPosition.class)))
                    .thenReturn(mockUpdate);

            InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);
            igm.setMap(mapMock);

            igm.centerOnCoordinates(updatedCoors);

            Mockito.verify(mapMock).animateCamera(mockUpdate, 200, null);
        } catch (Exception e) {
            Assert.fail("Assertion failure or exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testAddPolygons() {

        LatLng firstPoint = new LatLng(-1, -1);
        LatLng secondPoint = new LatLng(-1, 1);
        LatLng thirdPoint = new LatLng(1, -1);
        LatLng fourthPoint = new LatLng(1, 1);

        List<PolygonOptions> expectedPolygons = new ArrayList<>();

        PolygonOptions firstPolygon = new PolygonOptions()
                .add(firstPoint)
                .add(secondPoint)
                .add(thirdPoint)
                .add(fourthPoint);

        expectedPolygons.add(firstPolygon);

        InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);
        igm.setMap(mapMock);
        igm.addPolygons(expectedPolygons);

        Mockito.verify(mapMock).addPolygon(firstPolygon);
    }

    @Test
    public void testToggleLocationTrackingWithPermission() {
        InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);
        igm.setMap(mapMock);

        boolean res = igm.toggleLocationTracking(true);

        Assert.assertTrue(res);
    }

    @Test
    public void testToggleLocationTrackingWithoutPermission() {
        InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);
        igm.setMap(mapMock);

        Mockito.doThrow(new SecurityException()).when(mapMock).setMyLocationEnabled(true);
        boolean res = igm.toggleLocationTracking(true);

        Assert.assertFalse(res);
    }

    @Test
    public void testAddPolyline() {
        LatLng firstExpectedPoint = new LatLng(0,0);
        LatLng secondExpectedPoint = new LatLng(1,1);
        List<LatLng> expectedPointList = new ArrayList<>();
        expectedPointList.add(firstExpectedPoint);
        expectedPointList.add(secondExpectedPoint);
        PolylineOptions expectedOptions = new PolylineOptions()
                .add(firstExpectedPoint)
                .add(secondExpectedPoint);

        Polyline polyLine = Mockito.mock(Polyline.class);
        Mockito.when(polyLine.getPoints()).thenReturn(expectedPointList);
        Mockito.when(mapMock.addPolyline(expectedOptions)).thenReturn(polyLine);

        InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);
        igm.setMap(mapMock);

        igm.addPolyline(expectedOptions);

        List<Polyline> lines = igm.getPolylines();

        Assert.assertEquals(1, lines.size());

        Polyline actualLine = lines.get(0);

        Assert.assertEquals(polyLine.getPoints().size(), actualLine.getPoints().size());
        Assert.assertEquals(firstExpectedPoint, actualLine.getPoints().get(0));
        Assert.assertEquals(secondExpectedPoint, actualLine.getPoints().get(1));
    }

    @Test
    public void testAdd2PolylineWithDifferentColors() {
        //Create the first polyline information
        int firstExpectedPolylineColor = 1;
        LatLng firstExpectedPoint = new LatLng(0,0);
        LatLng secondExpectedPoint = new LatLng(1,1);
        List<LatLng> firstLineExpectedPointList = new ArrayList<>();
        firstLineExpectedPointList.add(firstExpectedPoint);
        firstLineExpectedPointList.add(secondExpectedPoint);
        PolylineOptions firstLineExpectedOptions = new PolylineOptions()
                .add(firstExpectedPoint)
                .add(secondExpectedPoint)
                .color(firstExpectedPolylineColor);

        Polyline firstPolyline = Mockito.mock(Polyline.class);
        Mockito.when(firstPolyline.getPoints()).thenReturn(firstLineExpectedPointList);
        Mockito.when(firstPolyline.getColor()).thenReturn(firstExpectedPolylineColor);

        //Create second polyline information
        int secondLineExpectedColor = 2;
        LatLng thirdExpectedPoint = new LatLng(2, 2);
        LatLng fourthExpectedPoint = new LatLng(3, 3);
        LatLng fifthExpectedPoint = new LatLng(4, 4);
        List<LatLng> secondLineExpectedPointList = new ArrayList<>();
        secondLineExpectedPointList.add(thirdExpectedPoint);
        secondLineExpectedPointList.add(fourthExpectedPoint);
        secondLineExpectedPointList.add(fifthExpectedPoint);
        PolylineOptions secondLineExpectedOptions = new PolylineOptions()
                .add(thirdExpectedPoint)
                .add(fourthExpectedPoint)
                .add(fifthExpectedPoint)
                .color(secondLineExpectedColor);

        Polyline secondPolyline = Mockito.mock(Polyline.class);
        Mockito.when(secondPolyline.getColor()).thenReturn(secondLineExpectedColor);
        Mockito.when(secondPolyline.getPoints()).thenReturn(secondLineExpectedPointList);

        //Create the map mock
        Mockito.when(mapMock.addPolyline(firstLineExpectedOptions)).thenReturn(firstPolyline);
        Mockito.when(mapMock.addPolyline(secondLineExpectedOptions)).thenReturn(secondPolyline);

        //Act
        InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);
        igm.setMap(mapMock);

        igm.addPolyline(firstLineExpectedOptions);
        igm.addPolyline(secondLineExpectedOptions);

        //Assert
        List<Polyline> lines = igm.getPolylines();

        Assert.assertEquals(2, lines.size());

        //First polyline tests
        Polyline actualFirstLine = lines.get(0);

        Assert.assertEquals(firstLineExpectedPointList.size(), actualFirstLine.getPoints().size());
        Assert.assertEquals(firstExpectedPoint, actualFirstLine.getPoints().get(0));
        Assert.assertEquals(secondExpectedPoint, actualFirstLine.getPoints().get(1));
        Assert.assertEquals(firstExpectedPolylineColor, actualFirstLine.getColor());

        //Second polyline tests
        Polyline actualSecondLine = lines.get(1);
        Assert.assertEquals(secondLineExpectedPointList.size(), actualSecondLine.getPoints().size());
        Assert.assertEquals(thirdExpectedPoint, actualSecondLine.getPoints().get(0));
        Assert.assertEquals(fourthExpectedPoint, actualSecondLine.getPoints().get(1));
        Assert.assertEquals(fifthExpectedPoint, actualSecondLine.getPoints().get(2));
        Assert.assertEquals(secondLineExpectedColor, actualSecondLine.getColor());
    }

    @Test
    public void testPolylineParseAndDisplayPath_ReturnsAPolyline() {
        int expectedLinesGenerated = 1;
        LatLng firstExpectedPoint = new LatLng(0,0);
        LatLng secondExpectedPoint = new LatLng(1, 1);
        List<LatLng> expectedPointList = new ArrayList<>();
        expectedPointList.add(firstExpectedPoint);
        expectedPointList.add(secondExpectedPoint);

        String encodedLine = PolyUtil.encode(expectedPointList);

        String jsonResponse = "{\n" +
                "    \"test\" : [\n" +
                "        {\n" +
                "            \"travelMode\": \"WALK\",\n" +
                "            \"polyline\": {\n" +
                "                \"encodedPolyline\": \"" + encodedLine + "\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";


        InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);
        igm.setMap(mapMock);

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray arr = jsonObject.getJSONArray("test");

            igm.parseRoutePolylineAndDisplay(arr);

            List<Polyline> actualLines = igm.getPolylines();

            //All we can check is that a polyline was inserted, there's no way to mock the internalGoogleMaps without defeating the point of the test
            Assert.assertEquals(expectedLinesGenerated, actualLines.size());
        }
        catch(JSONException e) {
            //Purposefully fail if there is a parsing exception
            Assert.fail("Exception while parsing the JSON: " + e.getMessage());
        }
    }

    @Test
    public void testPolylineParseAndDisplayPath_ReturnsPolylineTransit() {
        int expectedLinesGenerated = 1;
        LatLng firstExpectedPoint = new LatLng(0,0);
        LatLng secondExpectedPoint = new LatLng(1, 1);
        List<LatLng> expectedPointList = new ArrayList<>();
        expectedPointList.add(firstExpectedPoint);
        expectedPointList.add(secondExpectedPoint);

        String encodedLine = PolyUtil.encode(expectedPointList);

        String jsonResponse = "{\n" +
                "    \"steps\" : [\n" +
                "        {\n" +
                "            \"travelMode\": \"TRANSIT\",\n" +
                "            \"polyline\": {\n" +
                "                \"encodedPolyline\": \"" + encodedLine + "\"\n" +
                "            },\n" +
                "            \"transitDetails\": {\n" +
                "                \"transitLine\": {\n" +
                "                    \"vehicle\":{\n" +
                "                        \"type\": \"SUBWAY\"\n" +
                "                    },\n" +
                "                    \"name\": \"Ligne Verte\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";


        InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);
        igm.setMap(mapMock);

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray arr = jsonObject.getJSONArray("steps");

            igm.parseRoutePolylineAndDisplay(arr);

            List<Polyline> actualLines = igm.getPolylines();

            //All we can check is that a polyline was inserted, there's no way to mock the internalGoogleMaps without defeating the point of the test
            Assert.assertEquals(expectedLinesGenerated, actualLines.size());
        }
        catch(JSONException e) {
            //Purposefully fail if there is a parsing exception
            Assert.fail("Exception while parsing the JSON: " + e.getMessage());
        }
    }

    @Test
    public void testClearPathFromMap() {
        //Create the first polyline information
        LatLng firstExpectedPoint = new LatLng(0,0);
        LatLng secondExpectedPoint = new LatLng(1,1);
        List<LatLng> firstLineExpectedPointList = new ArrayList<>();
        firstLineExpectedPointList.add(firstExpectedPoint);
        firstLineExpectedPointList.add(secondExpectedPoint);
        PolylineOptions firstLineExpectedOptions = new PolylineOptions()
                .add(firstExpectedPoint)
                .add(secondExpectedPoint);

        Polyline firstPolyline = Mockito.mock(Polyline.class);

        //Create second polyline information
        LatLng thirdExpectedPoint = new LatLng(2, 2);
        LatLng fourthExpectedPoint = new LatLng(3, 3);
        LatLng fifthExpectedPoint = new LatLng(4, 4);
        List<LatLng> secondLineExpectedPointList = new ArrayList<>();
        secondLineExpectedPointList.add(thirdExpectedPoint);
        secondLineExpectedPointList.add(fourthExpectedPoint);
        secondLineExpectedPointList.add(fifthExpectedPoint);
        PolylineOptions secondLineExpectedOptions = new PolylineOptions()
                .add(thirdExpectedPoint)
                .add(fourthExpectedPoint)
                .add(fifthExpectedPoint);

        Polyline secondPolyline = Mockito.mock(Polyline.class);

        //Create the map mock
        Mockito.when(mapMock.addPolyline(firstLineExpectedOptions)).thenReturn(firstPolyline);
        Mockito.when(mapMock.addPolyline(secondLineExpectedOptions)).thenReturn(secondPolyline);

        //Act
        InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);
        igm.setMap(mapMock);

        igm.addPolyline(firstLineExpectedOptions);
        igm.addPolyline(secondLineExpectedOptions);

        //Assert
        List<Polyline> lines = igm.getPolylines();

        Assert.assertEquals(2, lines.size());

        igm.clearPathFromMap();

        lines = igm.getPolylines();

        Assert.assertEquals(0, lines.size());
    }

    @Test
    public void testInitialize() {
        InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);

        Fragment fragment = igm.initialize();

        Assert.assertNotNull(fragment);
    }

    @Test
    public void testSwitchToFloor() {
        InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);
        igm.setMap(mapMock);

        String floorName = "1";
        igm.switchToFloor(floorName);

        Mockito.verifyNoInteractions(mapMock);
        Mockito.verifyNoInteractions(mockListener);
    }

    @Test
    public void testOnMapReady() {
        InternalGoogleMaps igm = new InternalGoogleMaps(mockListener);

        igm.onMapReady(mapMock);

        Mockito.verify(mapMock, Mockito.atLeast(2)).addPolygon(Mockito.any(PolygonOptions.class));
        Mockito.verify(mapMock).setOnMapClickListener(Mockito.any(GoogleMap.OnMapClickListener.class));
        Mockito.verify(mockListener).onMapReady();
    }
}
