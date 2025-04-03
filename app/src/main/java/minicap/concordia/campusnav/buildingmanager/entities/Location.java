package minicap.concordia.campusnav.buildingmanager.entities;

import minicap.concordia.campusnav.map.MapCoordinates;

public class Location
{
    private MapCoordinates coordinates;
    private final double latitude;
    private final double longitude;

    protected Location(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.coordinates = new MapCoordinates(latitude, longitude);
    }

    /**
     * Returns the latitude and longitude of the location
     * @return A MapCoordinates object with latitude and longitude
     */
    public MapCoordinates getLocation() {
        return coordinates;
    }

    /**
     * Gets the latitude of the location
     * @return double representing latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude of the location
     * @return double representing longitude
     */
    public double getLongitude() {
        return longitude;
    }
}
