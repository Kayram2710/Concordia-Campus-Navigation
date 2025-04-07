package minicap.concordia.campusnav.helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import minicap.concordia.campusnav.map.MapCoordinates;

public class UserLocationService {

    private static final String TAG = "UserLocationService";

    private static MapCoordinates lastKnownLocation;

    private Context context;

    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;

    private UserLocationUpdatedListener listener;

    public UserLocationService(Context context, UserLocationUpdatedListener listener) {
        this.listener = listener;
        this.context = context;
        this.locationClient = LocationServices.getFusedLocationProviderClient(context);
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    lastKnownLocation = MapCoordinates.fromAndroidLocation(location);
                    listener.onUserLocationUpdated(lastKnownLocation);
                }
            }
        };
    }

    /**
     * Starts the user location tracking service
     * @param interval The interval that we should ideally receive updates (in milliseconds)
     *                 NOTE: The minimum interval for updates is half the interval
     */
    public void start(long interval) {
        long maxUpdateInterval = interval / 2;

        try {
            LocationRequest locationRequest = new LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, interval).setMinUpdateIntervalMillis(maxUpdateInterval).build();

            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while setting up location updates", e);
        }
    }

    /**
     * Gets the last known location of the user
     * @param context The context for the location service if needed to poll for last location
     * @param listener The listener who will receive the data update
     */
    public static void getLastKnownLocation(Context context, UserLocationUpdatedListener listener) {
        if (lastKnownLocation != null) {
            listener.onUserLocationUpdated(lastKnownLocation);
        }

        FusedLocationProviderClient temporaryClient = LocationServices.getFusedLocationProviderClient(context);
        try {
            temporaryClient.getLastLocation().addOnSuccessListener(location -> {
                listener.onUserLocationUpdated(MapCoordinates.fromAndroidLocation(location));
            });
        }
        catch (SecurityException e) {
            Log.d(TAG, "Location services not enabled");
        }
    }

    public interface UserLocationUpdatedListener {
        void onUserLocationUpdated(MapCoordinates newPosition);
    }
}
