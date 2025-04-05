package minicap.concordia.campusnav.map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.buildingshape.CampusBuildingShapes;
import minicap.concordia.campusnav.map.enums.MapColors;
import minicap.concordia.campusnav.map.helpers.MapColorConversionHelper;

public class InternalGoogleMaps extends AbstractMap implements OnMapReadyCallback, FetchPathTask.OnRouteFetchedListener {

    private final float defaultZoom = 18;
    private GoogleMap mMap;

    private List<Polyline> polylines = new ArrayList<>();

    private List<Marker> markers = new ArrayList<>();
    private List<Marker> poiMarkers = new ArrayList<>();
    private SupportMapFragment mapFrag;

    public InternalGoogleMaps(MapUpdateListener listener){
        super(listener);
    }

    @Override
    public Fragment initialize() {
        mapFrag = SupportMapFragment.newInstance();

        mapFrag.getMapAsync(this);

        return mapFrag;
    }

    /**
     * Returns the polylines of the current map
     * @return List of Polyline objects
     */
    public List<Polyline> getPolylines() {
        return polylines;
    }

    @Override
    public void centerOnCoordinates(MapCoordinates coordinates){
        resetCamera(coordinates);
    }
    @Override
    public void switchToFloor(String floorName) {
        //Google maps does not have floors, so do nothing
    }

    /**
     * Add polygons to the map (used for building shapes)
     * @param options A list of PolygonOptions to be added to the map
     */
    public void addPolygons(List<PolygonOptions> options) {
        for (PolygonOptions polygonOptions : options){
            mMap.addPolygon(polygonOptions);
        }
    }
    @Override
    public void addMarker(OutdoorPOI opoi){
        //Setting icon
        BitmapDescriptor icon = switch (opoi.getPOIType()) {
            case RESTAURANT ->
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_restaunrant_marker);
            case COFFEE_SHOP ->
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_coffee_marker);
            default ->
                    BitmapDescriptorFactory.defaultMarker();
            //TODO add icons for indoor POI
        };

        MarkerOptions newMarker = new MarkerOptions()
                .icon(icon)
                .position(opoi.getLocation().toGoogleMapsLatLng())
                .title(opoi.getPoiName());
        Marker poiMarker = mMap.addMarker(newMarker);
        poiMarker.setTag("POI");
        poiMarkers.add(poiMarker);
    }
    @Override
    public void addMarker(MapCoordinates position, String title, MapColors color, boolean clearOtherMarkers){
        if(clearOtherMarkers) {
            clearAllMarkers();
        }

        float markerColor = MapColorConversionHelper.getGoogleMapsColor(color);

        MarkerOptions newMarker = new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                                        .position(position.toGoogleMapsLatLng())
                                        .title(title);
        markers.add(mMap.addMarker(newMarker));
    }

    @Override
    public void addMarker(MapCoordinates position, String title) {
        addMarker(position, title, MapColors.DEFAULT, false);
    }

    @Override
    public void addMarker(MapCoordinates position, String title, MapColors color) {
        addMarker(position, title, color, false);
    }

    @Override
    public void addMarker(MapCoordinates position, String title, boolean clearOtherMarkers) {
        addMarker(position, title, MapColors.DEFAULT, clearOtherMarkers);
    }

    @Override
    public void clearAllMarkers() {
        for (Iterator<Marker> allMarkers = markers.iterator(); allMarkers.hasNext();) {
            Marker cur = allMarkers.next();
            cur.remove();
            allMarkers.remove();
        }
    }

    /**
     * Moves the camera to view all currently showing POI
     * @param position
     * @param bearing
     */
    public void moveCameraToPOI(MapCoordinates position, float bearing){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position.toGoogleMapsLatLng())
                .zoom(16)
                .bearing(bearing)
                .tilt(45)
                .build();

        mMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                200,
                null)
        ;
    }

    /**
     * Resets the map camera to a default view centered on the specified coordinates.
     *
     * @param position the target coordinates for resetting the camera position
     */
    private void resetCamera(MapCoordinates position){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position.toGoogleMapsLatLng())
                .zoom(defaultZoom)
                .bearing(0)
                .tilt(0)
                .build();

        mMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                200,
                null)
        ;
    }

    /**
     * Adds PolyLine to the googleMaps
     * @param options PolylineOptions
     */
    public void addPolyline(PolylineOptions options){
        polylines.add(mMap.addPolyline(options));
    }

    /**
     * Removes all polylines from the map (used for route)
     */
    @Override
    public void clearPathFromMap(){
        for (Iterator<Polyline> it = polylines.iterator(); it.hasNext();) {
            Polyline polyline = it.next();
            polyline.remove();
            it.remove();
        }
    }

    @Override
    public void displayRoute(MapCoordinates origin, MapCoordinates destination, String travelMode) {
        new FetchPathTask(this).fetchRoute(origin.toGoogleMapsLatLng(), destination.toGoogleMapsLatLng(), travelMode);
    }

    @Override
    public void displayPOI(MapCoordinates origin, POIType type){
        new FetchPathTask(this).fetchPOI(origin.toGoogleMapsLatLng(), type);
    }

    @Override
    public boolean toggleLocationTracking(boolean isEnabled) {
        try {
            mMap.setMyLocationEnabled(isEnabled);
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }

    /**
     * Parses the JSONArray given by the Routes API to create a polyline
     * @param steps The array of steps in the line
     * @throws JSONException Thrown when there is an error parsing the JSON
     */
    public void parseRoutePolylineAndDisplay(JSONArray steps) throws JSONException {
        // Looping trough steps to display on UI
        for (int i = 0; i < steps.length(); i++) {

            JSONObject step = steps.getJSONObject(i);
            String travelMode = step.getString("travelMode");
            JSONObject polylineObject = step.getJSONObject("polyline");
            String encodedPolyline = polylineObject.getString("encodedPolyline");
            List<LatLng> stepPoints = PolyUtil.decode(encodedPolyline);

            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(stepPoints)
                    .width(10);

            //Case where Walking
            if ("WALK".equals(travelMode)) {
                polylineOptions.color(Color.BLUE)
                        .pattern(Arrays.asList(new Dot(), new Gap(10)));
                //Case where Transit
            } else if ("TRANSIT".equals(travelMode)) {
                JSONObject transitDetails = step.getJSONObject("transitDetails");
                String vehicleType = transitDetails.getJSONObject("transitLine")
                        .getJSONObject("vehicle")
                        .getString("type");
                String transitLineName = transitDetails.getJSONObject("transitLine").getString("name");

                // Set color based on vehicle type
                if ("BUS".equalsIgnoreCase(vehicleType)) {
                    polylineOptions.color(Color.parseColor("#000000"));
                } else if ("SUBWAY".equalsIgnoreCase(vehicleType) || "METRO".equalsIgnoreCase(vehicleType)) {
                    switch (transitLineName) {
                        case "Ligne Verte", "Verte":
                            polylineOptions.color(Color.parseColor("#008000"));
                            break;
                        case "Ligne Orange", "Orange":
                            polylineOptions.color(Color.parseColor("#FFA500"));
                            break;
                        case "Ligne Jaune", "Jaune":
                            polylineOptions.color(Color.parseColor("#FFD700"));
                            break;
                        case "Ligne Bleu", "Bleu":
                            polylineOptions.color(Color.parseColor("#0000FF"));
                            break;
                        default:
                            polylineOptions.color(Color.GRAY); // Default in case the name changes
                            break;
                    }
                }
            }

            addPolyline(polylineOptions);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        setMap(googleMap);

        addPolygons(CampusBuildingShapes.getSgwBuildingCoordinates());
        addPolygons(CampusBuildingShapes.getLoyolaBuildingCoordinates());
        // disable default location button
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                listener.onMapClicked(MapCoordinates.fromGoogleMapsLatLng(latLng));
            }
        });

        listener.onMapReady();
    }

    @Override
    public void onRouteFetched(JSONArray info) {
        try {
            JSONArray steps = info.getJSONArray(0);
            String estimatedTimeValue = info.getString(1);
            listener.onEstimatedTimeUpdated(estimatedTimeValue);

            // Handles Route not fetched
            if (steps == null) {
                listener.onMapError("Error while fetching route, JSON info is null");
                return;
            }

            parseRoutePolylineAndDisplay(steps);

        } catch (JSONException e) {
            Log.e("Route Parsing Error", e.toString());
            listener.onMapError("Exception while parsing the google maps route: " + e.getMessage());
        }
    }

    @Override
    public void onPlacesFetched(List<OutdoorPOI> outdoorPOIS, MapCoordinates location) {
        //Deleting previous POI markers
        for (Iterator<Marker> allMarkers = poiMarkers.iterator(); allMarkers.hasNext();) {
            Marker cur = allMarkers.next();
            cur.remove();
            allMarkers.remove();
        }
        for(OutdoorPOI op : outdoorPOIS){
            addMarker(op);
        }
      moveCameraToPOI(location, calculatePathBearing(location));
    }

    /**
     * Sets the map (used for testing)
     * @param map The mocked google map
     */
    public void setMap(GoogleMap map) {
        mMap = map;
    }

    public GoogleMap getmMap(){
        return this.mMap;
    }

    public void setStyle(Context context, int resourceID){
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, resourceID));
    }

}
