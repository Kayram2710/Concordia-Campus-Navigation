package minicap.concordia.campusnav.buildingmanager.entities;

import minicap.concordia.campusnav.map.MapCoordinates;

public class Location
{
    private MapCoordinates coordinates;

    protected Location(MapCoordinates coordinates){
        this.coordinates = coordinates;
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
        return coordinates.getLat();
    }

    /**
     * Gets the longitude of the location
     * @return double representing longitude
     */
    public double getLongitude() {
        return coordinates.getLng();
    }

    /**
     * Gets the associated name of the location
     * @return String name
     */
    public String getLocationName() {
        return coordinates.getName();
    }
}
