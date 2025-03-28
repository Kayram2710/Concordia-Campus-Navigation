package minicap.concordia.campusnav.map;

import com.google.android.gms.maps.model.LatLng;
import com.mappedin.sdk.models.MPIMap;

public class MapCoordinates {
    private double lat;

    private double lng;

    private double x;

    private double y;

    public MapCoordinates(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        this.x = 0;
        this.y = 0;
    }

    public MapCoordinates(double lat, double lng, double x, double y) {
        this(lat, lng);
        this.x = x;
        this.y = y;
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
     * Creates a MapCoordinates object from MPIMap.MPICoordinate object
     * @param coordinate The MPIMap.MPICoordinate to be translated
     * @return new MapCoordinates object with the properties of the MPICoordinate
     */
    public static MapCoordinates fromMappedInCoordinate(MPIMap.MPICoordinate coordinate) {
        return new MapCoordinates(coordinate.getLatitude(), coordinate.getLongitude(), coordinate.getX(), coordinate.getY());
    }

    /**
     * Creates a LatLng based on the current MapCoordinates
     * @return new LatLng
     */
    public LatLng toGoogleMapsLatLng() {
        return new LatLng(this.lat, this.lng);
    }

    /**
     * Creates an MPIMap.MPICoordinate from the current MapCoordinates object
     * @param map The MPIMap this is applied to
     * @return new MPICoordinate with the properties of this object
     */
    public MPIMap.MPICoordinate toMappedInCoordinate(MPIMap map) {
        return new MPIMap.MPICoordinate(this.x, this.y, this.lat, this.lng, map);
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
     * Gets the x (used by MPICoordinate)
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y (used by MPICoordinate)
     * @return the y
     */
    public double getY() {
        return y;
    }
}
