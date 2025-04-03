package minicap.concordia.campusnav.map;

import android.content.Context;

import androidx.fragment.app.Fragment;

import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import java.util.List;

import minicap.concordia.campusnav.map.enums.MapColors;

public abstract class AbstractMap {

    protected MapUpdateListener listener;

    /**
     * Constructor
     * @param listener Listener for map events
     */
    public AbstractMap(MapUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * Starts the initialization process for the map and returns the fragment to display
     * @return The fragment used to display the map
     */
    public abstract Fragment initialize();
    public abstract void addMarker(OutdoorPOI opoi);
    /**
     * Adds a marker to the map with the specified parameters
     * @param position The position of the marker
     * @param title The title used to label the marker
     * @param color The color of the marker
     * @param clearOtherMarkers Flag to indicate whether to clear other markers
     */
    public abstract void addMarker(MapCoordinates position, String title, MapColors color, boolean clearOtherMarkers);

    /**
     * Adds a marker to the map of the specified color at the given position with the given title
     * @param position The position of the marker
     * @param title The title used to label the marker
     * @param color The color of the marker
     */
    public abstract void addMarker(MapCoordinates position, String title, MapColors color);

    /**
     * Adds a marker to the map at the given position with given title. Clears other markers if requested
     * @param position The position of the marker on the map
     * @param title The title used to label the marker
     * @param clearOtherMarkers Flag indicating whether to clear other markers
     */
    public abstract void addMarker(MapCoordinates position, String title, boolean clearOtherMarkers);

    /**
     * Adds a marker to the map at the given position with the given title
     * @param position The position of the marker on the map
     * @param title The title used to label the marker
     */
    public abstract void addMarker(MapCoordinates position, String title);

    /**
     * Clears all the markers from the map
     */
    public abstract void clearAllMarkers();

    /**
     * Clears the current path from the map
     */
    public abstract void clearPathFromMap();

    /**
     * Display a route from origin to destination on the map
     * @param origin The coordinates of the origin
     * @param destination The coordinates of the destination
     * @param travelMode The mode of travel to be used
     */
    public abstract void displayRoute(MapCoordinates origin, MapCoordinates destination, String travelMode);

    public abstract void displayPOI(MapCoordinates origin, POIType type);

    /**
     * Centers the map on the given coordinates
     * @param coordinates The coordinates used for centering
     */
    public abstract void centerOnCoordinates(MapCoordinates coordinates);

    /**
     * Switches the current floor to another one
     * @param floorName the name of the floor that's being switched to
     */
    public abstract void switchToFloor(String floorName);

    /**
     * Enables current location tracking on the map
     * @param isEnabled To enable or disable location tracking
     * @return True if successful, false otherwise
     */
    public abstract boolean toggleLocationTracking(boolean isEnabled);

    /**
     * Applies a custom style to the map using the specified style resource.
     * The style defines the visual appearance of map elements like roads, parks, and water.
     * @param context The application context used to access resources
     * @param resourceID The resource ID of the JSON style file (e.g., R.raw.map_style)
     */
    public void setStyle(Context context, int resourceID){
    }

    /**
     * Moves the map camera to the specified position with custom padding and view parameters.
     * The camera transition is animated for smooth movement.
     * @param padding The padding in pixels from the top of the map view (used to avoid UI overlap)
     * @param position The target geographic coordinates to center on
     * @param zoom The zoom level (typically between 2 for world view and 21 for building detail)
     * @param bearing The camera orientation in degrees (0 = north, 90 = east)
     */
    public void moveCameraToPosition(int padding, MapCoordinates position, float zoom, float bearing){
    }

    /**
     * Zooms the camera to show the entire route smoothly
     * @param allPoints List of coordinates that make up the route
     */
    public void zoomToRouteSmoothly(List<MapCoordinates> allPoints) {
    }

    /**
     * Creates and adds a user location marker
     * @param position The position of the marker
     * @param iconResId The resource ID for the marker icon
     */
    public void createUserMarker(MapCoordinates position, int iconResId, Context context){
    }

    /**
     * Updates an existing user marker's position
     * @param position The new position
     * @param context The current context
     */
    public void updateUserMarkerPosition(MapCoordinates position, Context context){

    }

    /**
     * Checks whether the user marker has been initialized and exists on the map.
     *
     * @return {@code true} if the user marker is null (not created),
     *         {@code false} if the marker exists
     */
    public boolean isUserMarkerNull() {
        return true;
    }

    /**
     * Rotates the user marker to the specified angle.
     * The rotation is applied relative to the marker's anchor point.
     * @param markerRotation The rotation angle in degrees (0-360), where 0 points north. Positive values rotate clockwise.
     */
    public void rotateUserMarker(float markerRotation) {

    }

    /**
     * Retrieves the geographic coordinates of the user marker's current position.
     *
     * @return The {@link MapCoordinates} of the user marker, or {@code null} if the marker doesn't exist
     */
    public MapCoordinates getMapCoordinateFromMarker() {
        return null;
    }

    /**
     * Clears all polylines from the map
     */
    public void clearAllPolylines(){
    }

    /**
     * Adds a polyline to the map using MapCoordinates
     * @param coordinates List of coordinates for the polyline
     * @param width Width of the polyline
     * @param color Color of the polyline
     * @param geodesic Whether the polyline should be geodesic
     */
    public void addPolyline(List<MapCoordinates> coordinates, int width, int color, boolean geodesic){
    }

    /**
     * Decodes an encoded polyline string into MapCoordinates
     * @param encodedPolyline The encoded polyline string
     * @return List of MapCoordinates
     */
    public List<MapCoordinates> decodePolyline(String encodedPolyline) {
        return null;
    }


    /**
     * Calculates the remaining distance along the current route from the specified location.
     * The calculation follows the path from the nearest point on the route to the destination.
     *
     * @param location The current location coordinates to calculate from
     * @return The remaining distance in meters along the route. Returns 0 if no route exists or if the location is at or beyond the destination.
     */
    public float calculateRemainingDistance(MapCoordinates location) {
        return 0;
    }

    /**
     * Calculates the bearing (direction) of the path at the specified position.
     * The bearing represents the direction of travel along the route at the nearest point.
     *
     * @param currentPosition The position to calculate the path bearing from
     * @return The bearing in degrees (0-360) relative to true north. Returns 0 if no route exists or if there's insufficient data to calculate the bearing.
     */
    public float calculatePathBearing(MapCoordinates currentPosition) {
        return 0;
    }



    public interface MapUpdateListener {
        /**
         * Method called when the map is ready for use
         */
        void onMapReady();

        /**
         * Method called when a new estimated time is given
         * @param newTime String representation of the new estimate
         */
        void onEstimatedTimeUpdated(String newTime);

        /**
         * Method called when there is an error with the map
         * @param errorString The error that happened as a string
         */
        void onMapError(String errorString);

        /**
         * Method called when the map is clicked
         * @param coordinates The coordinates of where the map was clicked
         */
        void onMapClicked(MapCoordinates coordinates);
    }
}
