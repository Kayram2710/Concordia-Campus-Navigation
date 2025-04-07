package minicap.concordia.campusnav.map;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapCoordinates implements Parcelable {
    public static final String DEFAULT_NAME = "UNKNOWN";

    private double lat;
    private double lng;

    private String name;

    public MapCoordinates(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        this.name = DEFAULT_NAME;
    }

    public MapCoordinates(double lat, double lng, String name) {
        this(lat, lng);
        this.name = name;
    }

    protected MapCoordinates(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.name = in.readString();
    }

    /**
     * Creates a MapCoordinates object from GoogleMaps Marker
     * @param marker The Marker to be detached
     * @return new MapCoordinates object with the location of the marker
     */
    public static MapCoordinates fromGoogleMapsMarker(Marker marker) {
        return new MapCoordinates(marker.getPosition().latitude, marker.getPosition().longitude);
    }

    /**
     * Creates a MapCoordinates object from GoogleMaps LatLng
     * @param coordinate The LatLng to be translated
     * @return new MapCoordinates object with the properties of the LatLng
     */
    public static MapCoordinates fromGoogleMapsLatLng(LatLng coordinate) {
        return new MapCoordinates(coordinate.latitude, coordinate.longitude);
    }

    /**
     * Creates a MapCoordinates object from an Android.Location
     * @param loc The location to transform
     * @return MapCoordinates for the given location
     */
    public static MapCoordinates fromAndroidLocation(Location loc) {
        return new MapCoordinates(loc.getLatitude(), loc.getLongitude());
    }

    /**
     * Creates a LatLng based on the current MapCoordinates
     * @return new LatLng
     */
    public LatLng toGoogleMapsLatLng() {
        return new LatLng(this.lat, this.lng);
    }

    /**
     * Gets the latitude
     * @return latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Gets the longitude
     * @return longitude
     */
    public double getLng() {
        return lng;
    }

    /**
     * Gets the name of the location
     * @return String name of the location
     */
    public String getName() {
        return name;
    }

    public static final Parcelable.Creator<MapCoordinates> CREATOR = new Creator<MapCoordinates>() {
        @Override
        public MapCoordinates createFromParcel(Parcel source) {
            return new MapCoordinates(source);
        }

        @Override
        public MapCoordinates[] newArray(int size) {
            return new MapCoordinates[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.name);
    }
}
