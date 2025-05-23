package minicap.concordia.campusnav.map;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import minicap.concordia.campusnav.BuildConfig;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

// Callback Interface
public class FetchPathTask {
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String LOCATION = "location";
    private static final String FETCH_POI_TAG = "FetchPOI()";

    private final OnRouteFetchedListener listener;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    /**
     * Constructor with Listener
     * @param listener OnRouteFetchedListener
     */
    public FetchPathTask(OnRouteFetchedListener listener) {
        this.listener = listener;
    }

    /**
     * Fetches the route given origin and destination.
     * Will invoke the listener for onRouteFetched with the route once complete
     * @param originObj LatLng
     * @param destinationObj LatLng
     */
    public void fetchRoute(LatLng originObj, LatLng destinationObj, String travelModeStr) {
        String urlString = "https://routes.googleapis.com/directions/v2:computeRoutes?key=" + BuildConfig.MAPS_API_KEY;

        JSONObject requestBody = new JSONObject();
        try {

            JSONObject origin = new JSONObject();
            JSONObject originLocation = new JSONObject();
            JSONObject originLatLng = new JSONObject();
            originLatLng.put(LATITUDE, originObj.latitude);
            originLatLng.put(LONGITUDE, originObj.longitude);
            originLocation.put("latLng", originLatLng);
            origin.put(LOCATION, originLocation);
            requestBody.put("origin", origin);

            JSONObject destination = new JSONObject();
            JSONObject destinationLocation = new JSONObject();
            JSONObject destinationLatLng = new JSONObject();
            destinationLatLng.put(LATITUDE, destinationObj.latitude);
            destinationLatLng.put(LONGITUDE, destinationObj.longitude);
            destinationLocation.put("latLng", destinationLatLng);
            destination.put(LOCATION, destinationLocation);
            requestBody.put("destination", destination);

            requestBody.put("travelMode", travelModeStr);


        } catch (JSONException e) {
            Log.e("FetchRoute(): ", e.toString());
        }

        executorService.execute(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("X-Goog-FieldMask","*");
                connection.setRequestProperty("X-Goog-Api-Key",BuildConfig.MAPS_API_KEY);
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);

                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                stream.close();

               JSONArray route = parseRoute(response.toString());

                mainThreadHandler.post(() -> {
                    if (listener != null) {
                        listener.onRouteFetched(route);
                    }
                });

            } catch (IOException e) {
                Log.e("FetchRoute(): ", e.toString());
            }
        });
    }


    /**
     * Given a json response from google API, it will parse the Route and return it
     * with the estimated route time
     * @param json response from google API
     * @return List of LatLng points
     */
    public JSONArray parseRoute(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray routes = jsonObject.getJSONArray("routes");
            if (routes.length() > 0) {
                JSONObject route = routes.getJSONObject(0);
                JSONArray legs = route.getJSONArray("legs");
                JSONObject leg = legs.getJSONObject(0);
                String duration = leg.getString("duration");
                JSONArray info = new JSONArray();
                info.put(leg.getJSONArray("steps"));
                info.put(convertSecondsToTime(duration));
                return info;
            }
        } catch (JSONException e) {
            Log.e("ParseRoute(): ", e.toString());
        }
        return null;
    }

    /**
     * Fetches information about nearby POIs that match the given type
     * @param originObj The origin of the search radius
     * @param type The type of POI that is desired
     */
    public void fetchPOI(LatLng originObj, POIType type) {
        String urlString = "https://places.googleapis.com/v1/places:searchNearby?key=" + BuildConfig.MAPS_API_KEY;
        Log.d(FETCH_POI_TAG,urlString);

        JSONObject requestBody = new JSONObject();
        try {

            requestBody.put("includedTypes", new JSONArray().put(type.getValue()));
            requestBody.put("maxResultCount", 10);

            JSONObject locationRestriction = new JSONObject();
            JSONObject circle = new JSONObject();
            JSONObject center = new JSONObject();
            center.put(LATITUDE, originObj.latitude);
            center.put(LONGITUDE, originObj.longitude);
            circle.put("center", center);
            circle.put("radius", 500);
            locationRestriction.put("circle", circle);
            requestBody.put("locationRestriction", locationRestriction);

        } catch (JSONException e) {
            Log.e(FETCH_POI_TAG, e.toString());
        }

        executorService.execute(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("X-Goog-FieldMask", "*");
                connection.setRequestProperty("X-Goog-Api-Key", BuildConfig.MAPS_API_KEY);
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = requestBody.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                Log.d(FETCH_POI_TAG, "Response Code: " + responseCode);
                InputStream stream;
                if (responseCode >= 200 && responseCode < 300) {
                    stream = connection.getInputStream();
                } else {
                    stream = connection.getErrorStream();
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                stream.close();
                if (responseCode >= 200 && responseCode < 300) {
                    List<OutdoorPOI> outdoorPOIS = parsePOI(response.toString(), type);
                    mainThreadHandler.post(() -> {
                        if (listener != null) {
                            listener.onPlacesFetched(outdoorPOIS, new MapCoordinates(originObj.latitude,originObj.longitude), type);
                        }
                    });
                }
            } catch (IOException e) {
                Log.e(FETCH_POI_TAG, "Exception: " + e.toString());
            }
        });
    }

    /**
     * Parses JSON response for desired POIs
     * @param json The json response from the query
     * @param type The type of POI that is wanted
     * @return List of OutdoorPOI that match the query
     */
    public List<OutdoorPOI> parsePOI(String json, POIType type) {
        List<OutdoorPOI> placesList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(json);
            JSONArray places = jsonResponse.getJSONArray("places");

            for (int i = 0; i < places.length(); i++) {
                JSONObject place = places.getJSONObject(i);
                String displayName = place.has("displayName")
                        ? place.getJSONObject("displayName").getString("text")
                        : "Unknown Place";
                JSONObject location = place.getJSONObject(LOCATION);
                float lat = (float)location.getDouble(LATITUDE);
                float lng = (float)location.getDouble(LONGITUDE);

                boolean wheelchairAccessible = true;
                if (place.has("accessibilityOptions")) {
                    JSONObject accessibilityOptions = place.getJSONObject("accessibilityOptions");

                    for (Iterator<String> it = accessibilityOptions.keys(); it.hasNext(); ) {
                        String key = it.next();
                        if(key.contains("wheelchairAccessible") && !accessibilityOptions.getBoolean(key)){
                            wheelchairAccessible = false;
                            break;
                        }
                    }
                }
                MapCoordinates coords = new MapCoordinates(lat, lng, displayName);
                placesList.add(new OutdoorPOI(coords, type, wheelchairAccessible));
            }
        } catch (JSONException e) {
            Log.e("parsePOI()", "Exception: " + e.toString());
        }
        return placesList;
    }

    /**
     * Converts seconds into a standard time format
     * @param secondsStr The seconds as a string
     * @return Formatted string of time
     */
    public String convertSecondsToTime(String secondsStr) {
        long seconds = Long.parseLong(secondsStr.replace("s", ""));
        long minutes = seconds / 60;
        long hours = minutes / 60;
        minutes = minutes % 60;

        if (hours > 0) {
            return String.format("%dh%dmin", hours, minutes);
        } else {
            return String.format("%dmin", minutes);
        }
    }
    /**
     * Listener for Google Api
     */
    public interface OnRouteFetchedListener {
        /**
         * Invoked when a route is fetched for the map
         * @param steps The steps of the route
         */
        void onRouteFetched(JSONArray steps);

        /**
         * Invoked when POIs of the given type are parsed
         * @param outdoorPOIS The list of pois
         * @param location The location of the origin
         * @param type The type of POI
         */
        void onPlacesFetched(List<OutdoorPOI> outdoorPOIS,  MapCoordinates location, POIType type);
    }
}