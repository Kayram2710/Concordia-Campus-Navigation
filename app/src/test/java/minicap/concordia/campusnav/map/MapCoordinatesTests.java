package minicap.concordia.campusnav.map;

import android.os.Parcel;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MapCoordinatesTests {

    @Test
    public void testGetLatitude_ReturnsCorrectLatitude() {
        double expectedLat = 12;
        double expectedLng = 0;

        MapCoordinates coords = new MapCoordinates(expectedLat, expectedLng);

        Assert.assertEquals(expectedLat, coords.getLat(), 0.0001);
    }

    @Test
    public void testGetLongitude_ReturnsCorrectLongitude() {
        double expectedLat = 0;
        double expectedLng = 12;

        MapCoordinates coordinates = new MapCoordinates(expectedLat, expectedLng);

        Assert.assertEquals(expectedLng, coordinates.getLng(), 0.0001);
    }

    @Test
    public void testGetName_ReturnsCorrectName() {
        double expectedLat = 0;
        double expectedLng = 12;
        String expectedName = "Test 1";

        MapCoordinates coordinates = new MapCoordinates(expectedLat, expectedLng, expectedName);

        Assert.assertEquals(expectedName, coordinates.getName());
    }

    @Test
    public void testCreateFromLatLng_ReturnsCorrectMapCoordinates() {
        double expectedLat = 12;
        double expectedLng = 15;

        LatLng expectedLatLng = new LatLng(expectedLat, expectedLng);

        MapCoordinates coordinates = MapCoordinates.fromGoogleMapsLatLng(expectedLatLng);

        Assert.assertEquals(expectedLat, coordinates.getLat(), 0.0001);
        Assert.assertEquals(expectedLng, coordinates.getLng(), 0.0001);
    }

    @Test
    public void testCreateLatLngFromCoordinates_ReturnsCorrectLatLng() {
        double expectedLat = 12;
        double expectedLng = 17;

        MapCoordinates coords = new MapCoordinates(expectedLat, expectedLng);

        LatLng actual = coords.toGoogleMapsLatLng();

        Assert.assertEquals(expectedLat, actual.latitude, 0.0001);
        Assert.assertEquals(expectedLng, actual.longitude, 0.0001);
    }

    @Test
    public void testWriteToParcel() {
        double expectedLat = 12;
        double expectedLng = 20;
        String expectedName = "Test 1";

        MapCoordinates expected = new MapCoordinates(expectedLat, expectedLng, expectedName);
        Parcel parcelMock = Mockito.mock(Parcel.class);

        expected.writeToParcel(parcelMock, 0);

        Mockito.verify(parcelMock, Mockito.atLeast(2)).writeDouble(Mockito.anyDouble());
        Mockito.verify(parcelMock).writeString(expectedName);
    }

    @Test
    public void testDescribeContents() {
        int expectedDescribeContents = 0;
        double expectedLat = 12;
        double expectedLng = 20;

        MapCoordinates actual = new MapCoordinates(expectedLat, expectedLng);

        Assert.assertEquals(expectedDescribeContents, actual.describeContents());
    }

    @Test
    public void testCreatorCreateFromParcel() {
        double expectedLat = 12;
        double expectedLng = 20;
        String expectedName = "Test 1";

        Parcel parcelMock = Mockito.mock(Parcel.class);
        Mockito.when(parcelMock.readDouble()).thenReturn(expectedLat, expectedLng);
        Mockito.when(parcelMock.readString()).thenReturn(expectedName);

        MapCoordinates actual = MapCoordinates.CREATOR.createFromParcel(parcelMock);

        Assert.assertEquals(expectedLat, actual.getLat(), 0.00001);
        Assert.assertEquals(expectedLng, actual.getLng(), 0.00001);
        Assert.assertEquals(expectedName, actual.getName());
    }

    @Test
    public void testCreatorNewArray() {
        int expectedSize = 2;

        MapCoordinates[] actual = MapCoordinates.CREATOR.newArray(expectedSize);

        Assert.assertEquals(expectedSize, actual.length);
    }
}
