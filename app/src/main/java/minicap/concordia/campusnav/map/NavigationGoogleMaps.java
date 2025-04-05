package minicap.concordia.campusnav.map;

import static minicap.concordia.campusnav.map.MapCoordinates.fromGoogleMapsLatLng;
import static minicap.concordia.campusnav.screens.NavigationActivity.DEFAULT_ZOOM;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;

public class NavigationGoogleMaps extends InternalGoogleMaps{

    private Marker userMarker;
    private int cameraPadding = 0;

    //================ Starter methods ================
    public NavigationGoogleMaps(MapUpdateListener listener) {
        super(listener);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        super.setMap(googleMap);

        //Set certain settings
        super.getmMap().getUiSettings().setZoomControlsEnabled(true);
        super.getmMap().getUiSettings().setCompassEnabled(true);
        super.getmMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);

        listener.onMapReady();
    }

    //================ Camera methods ================
    @Override
    public void moveCameraToPosition(int padding, MapCoordinates position, float zoom, float bearing){
        if(cameraPadding == 0){
            this.cameraPadding = padding;
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position.toGoogleMapsLatLng())
                .zoom(zoom)
                .bearing(bearing)
                .tilt(45)
                .build();

        super.getmMap().animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                200,
                null)
        ;

        super.getmMap().setPadding(0, padding, 0, 0);
    }

    @Override
    public void zoomToRouteSmoothly(List<MapCoordinates> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            return;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MapCoordinates coordinate : coordinates) {
            builder.include(coordinate.toGoogleMapsLatLng());
        }

        super.getmMap().animateCamera(
                CameraUpdateFactory.newLatLngBounds(builder.build(), 100),
                500,
                null
        );
    }

    //================ Marker Methods ================
    @Override
    public void createUserMarker(MapCoordinates position, int iconResId, Context context) {
        BitmapDescriptor icon = bitmapDescriptorFromVector(context, iconResId);
        if (icon == null) {
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        }

        userMarker = super.getmMap().addMarker(new MarkerOptions()
                        .position(position.toGoogleMapsLatLng())
                        .title("Your Location")
                        .icon(icon)
                        .anchor(0.5f, 0.5f)
                        .rotation(0)
                        .flat(false));
    }

    @Override
    public void rotateUserMarker(float bearing){
        if (!isUserMarkerNull()) {
            userMarker.setRotation(bearing);
        }
    }

    @Override
    public void updateUserMarkerPosition(MapCoordinates position, Context context){
        if (isUserMarkerNull()) {
            createUserMarker(position, R.drawable.token, context);
        } else {
            userMarker.setPosition(position.toGoogleMapsLatLng());
        }
    }

    @Override
    public boolean isUserMarkerNull(){
        return (userMarker == null);
    }

    @Override
    public MapCoordinates getMapCoordinateFromMarker(){
        return fromGoogleMapsLatLng(userMarker.getPosition());
    }


    //================ Polyline Methods ================

    @Override
    public void clearAllPolylines() {
        for (Polyline polyline : super.getPolylines()) {
            polyline.remove();
        }
        super.getPolylines().clear();
    }

    @Override
    public void addPolyline(List<MapCoordinates> coordinates, int width, int color, boolean geodesic) {
        List<LatLng> points = new ArrayList<>();
        for (MapCoordinates coord : coordinates) {
            points.add(coord.toGoogleMapsLatLng());
        }

        PolylineOptions options = new PolylineOptions()
                .addAll(points)
                .width(width)
                .color(color)
                .geodesic(geodesic);

        super.addPolyline(options);
    }

    @Override
    public List<MapCoordinates> decodePolyline(String encodedPolyline) {
        List<MapCoordinates> coordinates = new ArrayList<>();
        List<LatLng> points = PolyUtil.decode(encodedPolyline);
        for (LatLng point : points) {
            coordinates.add(MapCoordinates.fromGoogleMapsLatLng(point));
        }
        return coordinates;
    }

    /**
     * Gets the points from the first polyline (if exists)
     * @return List of LatLng points or empty list if no polylines
     */
    private List<LatLng> getFirstPolylinePoints() {
        if (super.getPolylines().isEmpty() || super.getPolylines().get(0) == null) {
            return new ArrayList<>();
        }
        return super.getPolylines().get(0).getPoints();
    }

    //================ Route Methods ================

    @Override
    public void displayRoute(MapCoordinates origin, MapCoordinates destination, String travelMode) {
        new FetchPathTask(new FetchPathTask.OnRouteFetchedListener() {
            @Override
            public void onRouteFetched(JSONArray routeInfo) {
                if (routeInfo == null) {
                    listener.onMapError("Route information is null");
                    return;
                }

                try {
                    if (routeInfo.length() < 2) {
                        throw new JSONException("Invalid route data");
                    }

                    clearAllPolylines();

                    JSONArray steps = routeInfo.getJSONArray(0);
                    String eta = routeInfo.optString(1, "N/A");

                    listener.onEstimatedTimeUpdated(eta);

                    // Display route steps
                    List<MapCoordinates> allPoints = new ArrayList<>();
                    for (int i = 0; i < steps.length(); i++) {
                        JSONObject polyline = steps.getJSONObject(i).getJSONObject("polyline");
                        String encodedPolyline = polyline.getString("encodedPolyline");
                        allPoints.addAll(decodePolyline(encodedPolyline));
                    }

                    if (!allPoints.isEmpty()) {
                        addPolyline(
                                allPoints,
                                20,
                                Color.parseColor("#4285F4"),
                                true
                        );
                        zoomToRouteSmoothly(allPoints);
                    }

                    if (!isUserMarkerNull()) {
                        MapCoordinates newPos = getMapCoordinateFromMarker();
                        float bearing = calculatePathBearing(newPos);
                        moveCameraToPosition(cameraPadding, newPos, DEFAULT_ZOOM, bearing);
                    }

                } catch (JSONException e) {
                    Log.e("Navigation", "Route display error", e);
                    listener.onMapError("Error displaying route");
                }
            }

            @Override
            public void onPlacesFetched(List<OutdoorPOI> outdoorPOIS,  MapCoordinates location) {

            }
        }).fetchRoute(origin.toGoogleMapsLatLng(), destination.toGoogleMapsLatLng(), travelMode);
    }


    //================ Calculation Methods ================
    @Override
    public float calculateRemainingDistance(MapCoordinates currentPosition) {
        List<LatLng> pathPoints = getFirstPolylinePoints();
        if (pathPoints.isEmpty()) return 0;

        float totalDistance = 0;
        boolean passedCurrentPos = false;
        LatLng currentLatLng = currentPosition.toGoogleMapsLatLng();

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            LatLng start = pathPoints.get(i);
            LatLng end = pathPoints.get(i + 1);

            if (!passedCurrentPos) {
                if (SphericalUtil.computeDistanceBetween(currentLatLng, end) <
                        SphericalUtil.computeDistanceBetween(currentLatLng, start)) {
                    passedCurrentPos = true;
                }
            }

            if (passedCurrentPos) {
                totalDistance += SphericalUtil.computeDistanceBetween(start, end);
            }
        }

        return totalDistance;
    }

    @Override
    public float calculatePathBearing(MapCoordinates currentPosition) {
        List<LatLng> pathPoints = getFirstPolylinePoints();
        if (pathPoints.size() < 2) {
            return 0;
        }

        float minDistance = Float.MAX_VALUE;
        LatLng closestPoint = null;
        LatLng nextPoint = null;
        LatLng currentLatLng = currentPosition.toGoogleMapsLatLng();

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            LatLng start = pathPoints.get(i);
            LatLng end = pathPoints.get(i + 1);

            double distance = SphericalUtil.computeDistanceBetween(currentLatLng, start);
            if (distance < minDistance) {
                minDistance = (float) distance;
                closestPoint = start;
                nextPoint = end;
            }
        }

        if (closestPoint == null || nextPoint == null) {
            return 0;
        }

        return (float) SphericalUtil.computeHeading(closestPoint, nextPoint);
    }


    //================ Bitmap methods ================

    /**
     * Converts a vector drawable resource into a BitmapDescriptor for use as a map marker icon.
     * The vector is rendered into a 100x100 pixel bitmap with ARGB_8888 configuration.
     *
     * @param context The application context used to access resources
     * @param vectorResId The resource ID of the vector drawable to convert
     * @return A BitmapDescriptor created from the vector drawable, or null if:
     *         - The drawable couldn't be loaded
     *         - The resource ID is invalid
     *         - Any error occurs during bitmap creation
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        try {
            Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
            if (vectorDrawable == null) {
                return null;
            }

            int width = 100;
            int height = 100;

            vectorDrawable.setBounds(0, 0, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        } catch (Exception e) {
            Log.e("Navigation", "Vector icon error", e);
            return null;
        }
    }


}
